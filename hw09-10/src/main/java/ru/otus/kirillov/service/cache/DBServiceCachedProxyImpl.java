package ru.otus.kirillov.service.cache;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.service.AbstractDBService;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.utils.CommonUtils;
import ru.otus.kirillov.utils.ReflectionUtils;

import static ru.otus.kirillov.utils.ReflectionUtils.instantiateWithInjections;

/** Прокси с кешом для реального DB-сервиса
 * Created by Александр on 12.02.2018.
 */
public class DBServiceCachedProxyImpl extends AbstractDBService {

    private DBService realDbService;

    private CacheEngine<String, DataSet> cacheEngine;

    public DBServiceCachedProxyImpl(CacheEngine<String, DataSet> cacheEngine, DBService realDbService) {
        this.realDbService = CommonUtils.retunIfNotNull(realDbService);
        this.cacheEngine = CommonUtils.retunIfNotNull(cacheEngine);
    }

    @Override
    protected Dao<?> createDao(Class<?> daoClass) {
        Dao<?> realDao = realDbService.getDao(ReflectionUtils.getSuperClassGenericArgument(daoClass));
        Dao<?> dao = instantiateWithInjections(daoClass, cacheEngine, realDao);
        if (dao == null) {
            throw new RuntimeException(
                    String.format("Unable to create DAO %s: try to check contract " +
                            "for DBServiceHibernateImpl", daoClass));
        }
        return dao;
    }
}
