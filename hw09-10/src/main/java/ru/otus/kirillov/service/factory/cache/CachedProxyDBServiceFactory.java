package ru.otus.kirillov.service.factory.cache;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.dao.cache.CachedProxyDao;
import ru.otus.kirillov.model.AddressDataSet;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.PhoneDataSet;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;
import ru.otus.kirillov.service.cache.DBServiceCachedProxyImpl;
import ru.otus.kirillov.service.factory.AbstractDBServiceFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Фабрика для создания {@link ru.otus.kirillov.service.cache.DBServiceCachedProxyImpl}
 * Created by Александр on 12.02.2018.
 */
public class CachedProxyDBServiceFactory extends AbstractDBServiceFactory {

    //region DAO-классы для модели из задания
    public static class UserDataSetDao extends CachedProxyDao<UserDataSet> {
        public UserDataSetDao() {}
        public UserDataSetDao(CacheEngine<String, DataSet> engine, Dao<UserDataSet> realDbDao) {
            super(engine, realDbDao);
        }
    }

    public static class AddressDataSetDao extends CachedProxyDao<AddressDataSet> {
        public AddressDataSetDao() {
        }

        public AddressDataSetDao(CacheEngine<String, DataSet> engine, Dao<AddressDataSet> realDbDao) {
            super(engine, realDbDao);
        }
    }

    public static class PhoneDataSetDao extends CachedProxyDao<PhoneDataSet> {
        public PhoneDataSetDao() {
        }

        public PhoneDataSetDao(CacheEngine<String, DataSet> engine, Dao<PhoneDataSet> realDbDao) {
            super(engine, realDbDao);
        }
    }

    private static List<Class<?>> DEFAULT_DAO_CLASSES =
            Arrays.asList(UserDataSetDao.class, AddressDataSetDao.class, PhoneDataSetDao.class);
    //endregion

    private DBServiceFactory realDBServiceFactory;
    private CacheEngine<String, DataSet> engine;

    public CachedProxyDBServiceFactory(DBServiceFactory realDBServiceFactory,
                                       CacheEngine<String, DataSet> engine) {
        this.realDBServiceFactory = CommonUtils.returnIfNotNull(realDBServiceFactory);
        this.engine = CommonUtils.returnIfNotNull(engine);
    }

    @Override
    protected List<Class<?>> getDefaultDaoClasses() {
        return DEFAULT_DAO_CLASSES;
    }

    @Override
    public DBService createDBService(DBServiceConfig config) {
        CommonUtils.requiredNotNull(config);
        DBService result = new DBServiceCachedProxyImpl(engine, realDBServiceFactory.createDBService(config));
        registerDAO(result, config);
        return result;
    }
}
