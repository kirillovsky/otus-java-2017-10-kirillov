package ru.otus.kirillov.dao.cache;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.List;
import java.util.function.LongFunction;

/** Прокся с кешированием к реальному DAO
 * Created by Александр on 11.02.2018.
 */
public abstract class CachedProxyDao<T extends DataSet> implements Dao<T> {

    private final LongFunction<String> ID_TO_KEY_MAPPER =
            id -> String.format("%s#%s", getEntityType().getSimpleName(), id);

    private CacheEngine<String, DataSet> engine;

    private Dao<T> readDaoService;

    public CachedProxyDao() {}

    public CachedProxyDao(CacheEngine<String, DataSet> engine, Dao<T> realDbDao) {
        this.engine = CommonUtils.retunIfNotNull(engine);
        this.readDaoService = CommonUtils.retunIfNotNull(realDbDao);
    }

    @Override
    public void delete(T object) {
        if(object == null) {
            return;
        }
        engine.delete(ID_TO_KEY_MAPPER.apply(object.getId()));
        readDaoService.delete(object);
    }

    @Override
    public T saveOrUpdate(T object) {
        if(object == null) {
            return null;
        }
        T result = readDaoService.saveOrUpdate(object);
        putToCache(result);
        return result;
    }

    @Override
    public T read(long id) {
        T result = getFromCache(id);
        if(result == null) {
            result = readDaoService.read(id);
            putToCache(result);
        }
        return result;
    }

    @Override
    public List<T> readAll() {
        List<T> result = readDaoService.readAll();
        result.stream().forEach(this::putToCache);
        return result;
    }

    private void putToCache(T entity) {
        if(entity != null) {
            engine.put(ID_TO_KEY_MAPPER.apply(entity.getId()), entity);
        }
    }

    private T getFromCache(long id) {
        return (T) engine.get(ID_TO_KEY_MAPPER.apply(id));
    }
}
