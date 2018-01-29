package ru.otus.kirillov.myorm;

import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.connection.ConnectionFactory;
import ru.otus.kirillov.myorm.connection.SingletonH2ConnectionFactory;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.List;

/**
 * Created by Александр on 29.01.2018.
 */
public class MyOrmServiceImpl implements MyOrmService{

    private ConnectionFactory connectionFactory;

    public MyOrmServiceImpl(DBServiceConfig config) {
        CommonUtils.requiredNotNull(config);
        connectionFactory = new SingletonH2ConnectionFactory(config.getConnectionString());
    }


    @Override
    public <T extends DataSet> void saveOrUpdate(T t) {

    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> loadAll(Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> void delete(T t) {

    }

    @Override
    public void destroy(){
        connectionFactory.closeConnections();
    }
}
