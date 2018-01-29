package ru.otus.kirillov.service.factory.myorm;

import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.dao.myorm.AbstractMyormDao;
import ru.otus.kirillov.model.AddressDataSet;
import ru.otus.kirillov.model.PhoneDataSet;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;
import ru.otus.kirillov.service.factory.AbstractDBServiceFactory;

import java.util.Arrays;
import java.util.List;

/** Фабрика для создания {@link DBServiceFactory} с реализацией через
 * MyORM
 * Created by Александр on 28.01.2018.
 */
public class MyOrmDBServiceFactory extends AbstractDBServiceFactory {

    //region DAO-классы для модели из задания
    public static class UserDataSetDao extends AbstractMyormDao<UserDataSet> {}
    public static class AddressDataSetDao extends AbstractMyormDao<AddressDataSet> {}
    public static class PhoneDataSetDao extends AbstractMyormDao<PhoneDataSet> {}
    //endregion

    private static final List<Class<?>> DEFAULT_DAO_CLASSES =
            Arrays.asList(UserDataSetDao.class, AddressDataSetDao.class, PhoneDataSetDao.class);

    @Override
    protected List<Class<?>> getDefaultDaoClasses() {
        return DEFAULT_DAO_CLASSES;
    }

    @Override
    public DBService createDBService(DBServiceConfig config) {
        return null;
    }
}
