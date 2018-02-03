package ru.otus.kirillov.myorm;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.delete.DeleteRequest;
import ru.otus.kirillov.myorm.commands.generateschema.GenerateSchemaRequest;
import ru.otus.kirillov.myorm.commands.saveOrUpdate.SaveOrUpdateRequest;
import ru.otus.kirillov.myorm.commands.select.SelectRequest;
import ru.otus.kirillov.myorm.connection.ConnectionFactory;
import ru.otus.kirillov.myorm.connection.SingletonH2ConnectionFactory;
import ru.otus.kirillov.myorm.schema.OrmMapper;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Александр on 29.01.2018.
 */
public class MyOrmServiceImpl implements MyOrmService{

    private ConnectionFactory connectionFactory;

    private OrmMapper mapper;

    private CommandInvoker invoker;

    public MyOrmServiceImpl(DBServiceConfig config, List<Class<? extends DataSet>> entityClasses) {
        CommonUtils.requiredNotNull(config);
        connectionFactory = new SingletonH2ConnectionFactory(config.getConnectionString());
        invoker = new CommandInvoker(connectionFactory.getConnection());
        mapper = createOrmMapper(entityClasses);
    }


    @Override
    public <T extends DataSet> void saveOrUpdate(T t) {
        CommonUtils.requiredNotNull(t);
        EntityDescriptor descriptor = mapper.getDescription(t.getClass());
        invoker.invoke(new SaveOrUpdateRequest(descriptor, EntityDescriptor.getAllFieldValuePair(descriptor, t)));

    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        CommonUtils.requiredNotNull(clazz);
        EntityDescriptor descriptor = mapper.getDescription(clazz);
        List<T> resultList = invoker.invoke(
                new SelectRequest(descriptor,
                        Collections.singletonList(Pair.of(descriptor.getGeneratedIdField(), id))
                )
        );

        if(resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    @Override
    public <T extends DataSet> List<T> loadAll(Class<T> clazz) {
        CommonUtils.requiredNotNull(clazz);
        EntityDescriptor descriptor = mapper.getDescription(clazz);
        return invoker.invoke(new SelectRequest(descriptor, Collections.emptyList()));
    }

    @Override
    public <T extends DataSet> void delete(T t) {
        CommonUtils.requiredNotNull(t);
        EntityDescriptor descriptor = mapper.getDescription(t.getClass());
        invoker.invoke(new DeleteRequest(descriptor,
            Collections.singletonList(Pair.of(descriptor.getGeneratedIdField(), t.getId()))
        ));
    }

    @Override
    public void destroy(){
        connectionFactory.closeConnections();
    }

    private OrmMapper createOrmMapper(List<Class<? extends DataSet>> entityClasses) {
        OrmMapper mapper = new OrmMapper();
        Map<Class<? extends DataSet>, EntityDescriptor> entityClassMap = new HashMap<>();
        entityClasses.forEach(aClass ->
           entityClassMap.putAll(invoker.invoke(new GenerateSchemaRequest(aClass, entityClassMap)))
        );
        mapper.addAll(entityClassMap);
        return mapper;
    }
}
