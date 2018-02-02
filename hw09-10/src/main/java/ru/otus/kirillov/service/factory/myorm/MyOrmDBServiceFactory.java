package ru.otus.kirillov.service.factory.myorm;

import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.dao.myorm.AbstractMyOrmDao;
import ru.otus.kirillov.model.AddressDataSet;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.PhoneDataSet;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.myorm.MyOrmService;
import ru.otus.kirillov.myorm.MyOrmServiceImpl;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;
import ru.otus.kirillov.service.factory.AbstractDBServiceFactory;
import ru.otus.kirillov.service.myorm.DBServiceMyOrmImpl;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Фабрика для создания {@link DBServiceFactory} с реализацией через
 * MyORM
 * Created by Александр on 28.01.2018.
 */
public class MyOrmDBServiceFactory extends AbstractDBServiceFactory {

    //region DAO-классы для модели из задания
    public static class UserDataSetDao extends AbstractMyOrmDao<UserDataSet> {
    }

    public static class AddressDataSetDao extends AbstractMyOrmDao<AddressDataSet> {
    }

    public static class PhoneDataSetDao extends AbstractMyOrmDao<PhoneDataSet> {
    }
    //endregion

    private static final List<Class<?>> DEFAULT_DAO_CLASSES =
            Arrays.asList(UserDataSetDao.class, AddressDataSetDao.class, PhoneDataSetDao.class);

    @Override
    protected List<Class<?>> getDefaultDaoClasses() {
        return DEFAULT_DAO_CLASSES;
    }

    @Override
    public DBService createDBService(DBServiceConfig config) {
        CommonUtils.requiredNotNull(config);
        List<Class<? extends DataSet>> entityClasses =
                getDaoClasses(config.getUsersDaoClasses())
                        .map(clazz -> (Class<? extends DataSet>) getDaoTypeArgument(clazz))
                        .collect(Collectors.toList());

        MyOrmService ormService = new MyOrmServiceImpl(config, entityClasses);
        DBService dbService = new DBServiceMyOrmImpl(ormService);
        registerDAO(dbService, config);
        return dbService;
    }
}
