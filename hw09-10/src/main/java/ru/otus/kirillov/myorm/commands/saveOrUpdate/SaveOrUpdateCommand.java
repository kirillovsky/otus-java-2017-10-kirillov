package ru.otus.kirillov.myorm.commands.saveOrUpdate;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.delete.DeleteRequest;
import ru.otus.kirillov.myorm.commands.generatesql.GenerateSQLRequest;
import ru.otus.kirillov.myorm.commands.select.SelectRequest;
import ru.otus.kirillov.myorm.executors.DmlExecutor;
import ru.otus.kirillov.myorm.schema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors.OneToManyFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors.OneToOneFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors.SyntheticFieldDescriptor;

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
 * 5. Рекурсивно делаем delete по back-reference id, а потом
 * saveOrUpdate для полей {@link javax.persistence.OneToMany}
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
            updateOneToOneFields(rq.getFieldValueByDescriptor());
            //2. Селектим по id нужную сущность
            boolean needToSave = selectEntities(rq.getDescriptor(), getId(rq.getFieldValueByDescriptor())).isEmpty();
            if (needToSave) {
                //3. Если результат пуст - делаем сейв (сейвим все необходимые поля)
                performSave(rq.getDescriptor(), createPreparationFieldsForInsert(rq.getFieldValueByDescriptor()));
            } else {
                //4. Если нет - делаем апдейт (все нужные поля по id)
                performUpdate(rq.getDescriptor(), createPreparationFieldsForUpdate(rq.getFieldValueByDescriptor()));
            }

            //5. Рекурсивно делаем delete по back-reference id, а потом
            //saveOrUpdate для полей {@link javax.persistence.OneToMany}
            updateOneToManyFields(rq.getFieldValueByDescriptor(), getId(rq.getFieldValueByDescriptor()));

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

    private void updateOneToOneFields(List<Pair<AbstractFieldDescriptor, Object>> updatedObject) {
        updatedObject.stream()
                .filter(p -> p.getKey().isOneToOneField())
                .forEach(p -> {
                    OneToOneFieldDescriptor fieldDescriptor = (OneToOneFieldDescriptor) p.getKey();
                    DataSet refObject = (DataSet) p.getValue();

                    if (refObject == null) {
                        return;
                    }

                    List<Pair<AbstractFieldDescriptor, Object>> preparedValueList =
                            EntityDescriptor.getAllFieldValuePair(fieldDescriptor.getEntityDescriptor(), refObject);
                    getInvoker().invoke(new SaveOrUpdateRequest(fieldDescriptor.getEntityDescriptor(), preparedValueList, false));
                });
    }

    private void updateOneToManyFields(List<Pair<AbstractFieldDescriptor, Object>> updatedObject, long parentEntityId) {
        updatedObject.stream()
                .filter(p -> p.getKey().isOneToManyField())
                .forEach(p -> {
                    OneToManyFieldDescriptor fieldDescriptor = (OneToManyFieldDescriptor) p.getKey();
                    deleteViaBackRef(fieldDescriptor.getEntityDescriptor(), fieldDescriptor.getBackRefIdFieldDescriptor(),
                            parentEntityId);
                    Collection<? extends DataSet> refObjects =
                            (Collection<? extends DataSet>) p.getValue();

                    if (refObjects == null) {
                        return;
                    }

                    for (DataSet backRefObj : refObjects) {
                        List<Pair<AbstractFieldDescriptor, Object>> preparedValueList =
                                EntityDescriptor.getAllFieldValuePair(fieldDescriptor.getEntityDescriptor(), backRefObj).stream()
                                        .map(pair -> {
                                            if (pair.getKey() == fieldDescriptor.getBackRefIdFieldDescriptor()) {
                                                return Pair.<AbstractFieldDescriptor, Object>of(pair.getKey(), parentEntityId);
                                            } else {
                                                return pair;
                                            }
                                        })
                                        .collect(Collectors.toList());
                        getInvoker().invoke(
                                new SaveOrUpdateRequest(fieldDescriptor.getEntityDescriptor(), preparedValueList, false)
                        );
                    }
                });
    }

    private List<? extends DataSet> selectEntities(EntityDescriptor entityDescriptor, Object idValue) {
        return getInvoker().invoke(
                new SelectRequest(entityDescriptor,
                        Collections.singletonList(Pair.of(entityDescriptor.getGeneratedIdField(), idValue))
                )
        );
    }

    private void performSave(EntityDescriptor entityDescriptor, List<Pair<AbstractFieldDescriptor, Object>> updatedObject) {
        String insertQuery = getInvoker().invoke(GenerateSQLRequest.insertRequest(entityDescriptor));
        executeDml(insertQuery, updatedObject);
    }

    private void performUpdate(EntityDescriptor entityDescriptor, List<Pair<AbstractFieldDescriptor, Object>> updatedObject) {
        String updateQuery =
                getInvoker().invoke(GenerateSQLRequest.updateRequest(entityDescriptor,
                        entityDescriptor.getSqlVisibleFields().stream()
                                .filter(f -> f.isGeneratedIdField())
                                .collect(Collectors.toList())
                        )
                );
        executeDml(updateQuery, updatedObject);
    }

    private void executeDml(String dmlQuery, List<Pair<AbstractFieldDescriptor, Object>> preparationStatementPairs) {
        new DmlExecutor(getInvoker().getConnection()).execute(dmlQuery, preparationStatementPairs);
    }

    private List<Pair<AbstractFieldDescriptor, Object>>
    createPreparationFieldsForInsert(List<Pair<AbstractFieldDescriptor, Object>> allPreparedField) {
        List<Pair<AbstractFieldDescriptor, Object>> result = new ArrayList<>();

        List<AbstractFieldDescriptor> oneToOneSyntheticFields =
                allPreparedField.stream()
                        .filter(p -> p.getKey().isOneToOneField())
                        .map(p -> ((OneToOneFieldDescriptor) p.getKey()).getRefIdFieldDescriptor())
                        .collect(Collectors.toList());

        for (Pair<AbstractFieldDescriptor, Object> p : allPreparedField) {
            AbstractFieldDescriptor f = p.getKey();
            if (f.isSQLVisibleFields() && !oneToOneSyntheticFields.contains(f)) {
                result.add(p);
            } else if (f.isOneToOneField()) {
                Long refIdValue = p.getValue() == null ? null : ((DataSet) p.getValue()).getId();
                result.add(Pair.of(((OneToOneFieldDescriptor) f).getRefIdFieldDescriptor(), refIdValue));
            }
        }
        return result;
    }

    private List<Pair<AbstractFieldDescriptor, Object>>
    createPreparationFieldsForUpdate(List<Pair<AbstractFieldDescriptor, Object>> allPreparedField) {
        List<Pair<AbstractFieldDescriptor, Object>> result =
                createPreparationFieldsForInsert(allPreparedField).stream()
                        .filter(p -> !p.getKey().isGeneratedIdField())
                        .collect(Collectors.toList());

        Pair<AbstractFieldDescriptor, Object> whereField =
                allPreparedField.stream()
                        .filter(p -> p.getKey().isGeneratedIdField())
                        .findAny().orElseThrow(() -> new IllegalStateException("Not found generated id field"));

        result.add(whereField);
        return result;
    }

    private Long getId(List<Pair<AbstractFieldDescriptor, Object>> fieldsValuePair) {
        return fieldsValuePair.stream()
                .filter(p -> p.getKey().isGeneratedIdField())
                .mapToLong(p -> (Long) p.getValue())
                .findAny().orElseThrow(IllegalStateException::new);
    }

    private void deleteViaBackRef(EntityDescriptor descriptor, SyntheticFieldDescriptor backRefId, long parentEntityId) {
        getInvoker().invoke(
                new DeleteRequest(descriptor, Collections.singletonList(Pair.of(backRefId, parentEntityId)), false)
        );
    }


}
