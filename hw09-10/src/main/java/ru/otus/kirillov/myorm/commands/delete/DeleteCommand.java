package ru.otus.kirillov.myorm.commands.delete;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.generatesql.GenerateSQLRequest;
import ru.otus.kirillov.myorm.commands.select.SelectRequest;
import ru.otus.kirillov.myorm.executors.DmlExecutor;
import ru.otus.kirillov.myorm.mapper.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.mapper.EntityDescriptor;
import ru.otus.kirillov.utils.ReflectionUtils;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Алгоритм:
 * 1. По полученным данным селектим сущность
 * 2. Удаляем OneToOne по id, который получаем из заселекченного объекта на шаге 1 (без коммита)
 * 3. Удаляем OneToMany по id-шникам, полученным и заселекченного объекта на шаге 1 (без коммита)
 * 4. Генерируем и выполняем запрос на удаление по входным данным
 * 5. Если все ок - коммит
 * 6. Иначе - роллбэк
 * Created by Александр on 31.01.2018.
 */
public class DeleteCommand extends AbstractCommand<DeleteRequest, Void> {


    public DeleteCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public Void execute(DeleteRequest rq) {
        Connection connection = getInvoker().getConnection();
        try {
            //1. По полученным данным селектим сущность
            getDeletedEntities(rq.getEntityDescriptor(), rq.getWhereClojure()).forEach(
                    o -> {
                        //2. Удаляем OneToOne по id, который получаем из заселекченного объекта на шаге 1 (без коммита)
                        deleteOneToOne(rq.getEntityDescriptor(), o);
                        //3. Удаляем OneToMany по id-шникам, полученным и заселекченного объекта на шаге 1 (без коммита)
                        deleteOneToMany(rq.getEntityDescriptor(), o);
                        //4. Генерируем и выполняем запрос на удаление по входным данным
                        String deleteQuery = createQuery(rq);
                        new DmlExecutor(getConnection()).execute(deleteQuery, rq.getWhereClojure());
                    }
            );

        } catch (Exception e) {
            //6. Eсли не все ок - роллбэк
            unhandled(connection, con -> con.rollback());
            throw new RuntimeException(e);
        } finally {
            //5. Если все ок - коммит
            unhandled(connection, con -> con.commit());
        }

        return null;
    }

    private List<? extends DataSet> getDeletedEntities(EntityDescriptor entityDescriptor,
                                                       List<Pair<AbstractFieldDescriptor, Object>> whereClojure) {
        return getInvoker().invoke(new SelectRequest(entityDescriptor, whereClojure));
    }

    private void deleteOneToOne(EntityDescriptor descriptor, DataSet selectedObject) {
        descriptor.getOneToOneFields().forEach(
                f -> {
                    EntityDescriptor entityDescriptor = f.getEntityDescriptor();
                    getInvoker().invoke(
                            new DeleteRequest(entityDescriptor, Collections.singletonList(
                                    Pair.of(entityDescriptor.getGeneratedIdField(),
                                            ReflectionUtils.getFieldValue(f.getJavaField(), selectedObject)
                                    )
                            ), false)
                    );
                }
        );
    }

    @SuppressWarnings("unchecked")
    private void deleteOneToMany(EntityDescriptor descriptor, DataSet selectedObject) {
        descriptor.getOneToManyFields().forEach(
                f -> {
                    EntityDescriptor entityDescriptor = f.getEntityDescriptor();
                    Collection<? extends DataSet> col =
                            (Collection<? extends DataSet>) ReflectionUtils.getFieldValue(f.getJavaField(), selectedObject);
                    col.forEach(
                            o -> getInvoker().invoke(
                                    new DeleteRequest(entityDescriptor, Collections.singletonList(
                                            Pair.of(entityDescriptor.getGeneratedIdField(), o.getId())
                                    ), false)
                            )
                    );
                }
        );
    }

    private String createQuery(DeleteRequest rq) {
        return getInvoker().invoke(
                GenerateSQLRequest.deleteRequest(rq.getEntityDescriptor(), rq.getWhereClojure().stream()
                        .map(p -> p.getKey())
                        .collect(Collectors.toList())
                )
        );
    }
}
