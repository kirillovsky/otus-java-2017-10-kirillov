package ru.otus.kirillov.myorm.commands.saveOrUpdate;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.generatesql.GenerateSQLRequest;
import ru.otus.kirillov.myorm.commands.select.SelectRequest;
import ru.otus.kirillov.myorm.executors.DmlExecutor;
import ru.otus.kirillov.myorm.shema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.shema.elements.EntityDescriptor;
import ru.otus.kirillov.utils.ReflectionUtils;
import ru.otus.kirillov.myorm.shema.elements.FieldDescriptors.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Алгоритм:
 * На вход получаем сохраняемый entity и дескриптор сущности.
 * 1. Рекурсивно делаем savOrUpdate полей {@link javax.persistence.OneToOne}
 * так как гипотетически можем ссылаться на на них
 * 2. Селектим по id нужную сущность
 * 3. Если результат пуст - делаем сейв (сейвим все необходимые поля)
 * 4. Если нет - делаем апдейт (все нужные поля по id)
 * 5. Рекурсивно делаем saveOrUpdate для полей {@link javax.persistence.OneToMany}
 * 6. Если все ок - коммит
 * 7. Если нет - роллбэк
 * Created by Александр on 01.02.2018.
 */
public class SaveOrUpdateCommand extends AbstractCommand<SaveOrUpdateRequest, Void> {

    public SaveOrUpdateCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public Void execute(SaveOrUpdateRequest rq) {
        Connection connection = getInvoker().getConnection();
        try {
            //1. Рекурсивно делаем savOrUpdate полей {@link javax.persistence.OneToOne}
            updateOneToOneFields(rq.getDescriptor(), rq.getObject());
            //2. Селектим по id нужную сущность
            boolean needToSave = selectEntities(rq.getDescriptor(), rq.getObject().getId()).isEmpty();
            if (needToSave) {
                //3. Если результат пуст - делаем сейв (сейвим все необходимые поля)
                performSave(rq.getDescriptor(), rq.getObject());
            } else {
                //4. Если нет - делаем апдейт (все нужные поля по id)
                performUpdate(rq.getDescriptor(), rq.getObject());
            }

            //5. Рекурсивно делаем saveOrUpdate для полей {@link javax.persistence.OneToMany}
            updateOneToManyFields(rq.getDescriptor(), rq.getObject());

        } catch (Exception e) {
            //7. Eсли не все ок - роллбэк
            if (rq.isNeedsToCommit()) {
                unhandled(connection, con -> con.rollback());
            }
            throw new RuntimeException(e);
        } finally {
            //6. Если все ок - коммит
            if (rq.isNeedsToCommit()) {
                unhandled(connection, con -> con.commit());
            }
        }
        return null;
    }

    private void updateOneToOneFields(EntityDescriptor entityDescriptor, DataSet updatedObject) {
        entityDescriptor.getOneToOneFields()
                .forEach(f -> {
                    DataSet oneToOneRefObj = (DataSet) ReflectionUtils.getFieldValue(f.getJavaField(), updatedObject);
                    getInvoker().invoke(new SaveOrUpdateRequest(f.getEntityDescriptor(), oneToOneRefObj, false));
                });
    }

    private void updateOneToManyFields(EntityDescriptor entityDescriptor, DataSet updatedObject) {
        entityDescriptor.getOneToManyFields()
                .forEach(f -> {
                    Collection<? extends DataSet> oneToManeRefCollection =
                            (Collection<? extends DataSet>) ReflectionUtils.getFieldValue(f.getJavaField(), updatedObject);
                    oneToManeRefCollection.stream()
                            .forEach(obl -> getInvoker().invoke(
                                    new SaveOrUpdateRequest(f.getEntityDescriptor(), obl, false))
                            );
                });
    }

    private List<? extends DataSet> selectEntities(EntityDescriptor entityDescriptor, Object idValue) {
        return getInvoker().invoke(
                new SelectRequest(entityDescriptor,
                        Collections.singletonList(Pair.of(entityDescriptor.getGeneratedIdField(), idValue))
                )
        );
    }

    private void performSave(EntityDescriptor entityDescriptor, DataSet object) {
        String insertQuery = getInvoker().invoke(GenerateSQLRequest.insertRequest(entityDescriptor));
        executeDml(insertQuery, createPreparationFieldsForInsert(entityDescriptor, object));
    }

    private void performUpdate(EntityDescriptor entityDescriptor, DataSet object) {
        String updateQuery =
                getInvoker().invoke(GenerateSQLRequest.updateRequest(entityDescriptor, entityDescriptor.getSqlVisibleFields()));
        executeDml(updateQuery, createPreparationFieldsForUpdateById(entityDescriptor, object));
    }

    private void executeDml(String dmlQuery, List<Pair<AbstractFieldDescriptor, Object>> preparationStatementPairs) {
        new DmlExecutor(getInvoker().getConnection()).execute(dmlQuery, preparationStatementPairs);
    }

    private List<Pair<AbstractFieldDescriptor, Object>>
    createPreparationFieldsForInsert(EntityDescriptor descriptor, DataSet object) {
        List<Pair<AbstractFieldDescriptor, Object>> result = new ArrayList<>();

        for (AbstractFieldDescriptor f : descriptor.getFieldDescriptors()) {
            if (f.isGeneratedIdField() || f.isPrimitiveField()) {
                result.add(Pair.of(f, ReflectionUtils.getFieldValue(f.getJavaField(), object)));
            } else if (f.isOneToOneField()) {
                result.add(
                        Pair.of(
                                ((OneToOneFieldDescriptor) f).getRefIdFieldDescriptor(),
                                ((DataSet) ReflectionUtils.getFieldValue(f.getJavaField(), object)).getId()
                        )
                );
            }
        }

        return result;
    }

    private List<Pair<AbstractFieldDescriptor, Object>>
    createPreparationFieldsForUpdateById(EntityDescriptor descriptor, DataSet object) {
        List<Pair<AbstractFieldDescriptor, Object>> result =
        createPreparationFieldsForInsert(descriptor, object).stream()
                .filter(p -> !p.getKey().isGeneratedIdField())
                .collect(Collectors.toList());

        result.add(Pair.of(descriptor.getGeneratedIdField(), object.getId()));
        return result;
    }


}
