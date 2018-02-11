package ru.otus.kirillov.dao.cache;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.LongFunction;

/**
 * Created by Александр on 11.02.2018.
 */
public abstract class CachedDao<T extends DataSet> implements Dao<T> {

    private final LongFunction<String> ID_TO_KEY_MAPPER =
            id -> String.format("%s#%s", getEntityType().getSimpleName(), id);

    private final CacheEngine<String, T> engine;

    private final Dao<T> readDaoService;

    public CachedDao(CacheEngine<String, T> engine, Dao<T> realDbDao) {
        this.engine = CommonUtils.retunIfNotNull(engine);
        this.readDaoService = CommonUtils.retunIfNotNull(realDbDao);
    }

    @Override
    public void delete(T object) {

    }

    @Override
    public T saveOrUpdate(T object) {
        return null;
    }

    @Override
    public T read(long id) {
        T result = getFromCache(id);
        if(result == null) {
            result = readDaoService.read(id);
            if(result != null) {
                putToCache();
            }
        }

        return result != null ? result: readDaoService.read(id);
    }

    @Override
    public List<T> readAll() {
        List<T> result = readDaoService.readAll();
        result.stream()
                .filter(entity -> entity != null)
                .forEach(this::putToCache);
        return result;
    }

    private void putToCache(T entity) {
        if(entity != null) {
            engine.put(ID_TO_KEY_MAPPER.apply(entity.getId()), entity);
        }
    }

    private T getFromCache(long id) {
        return engine.get(ID_TO_KEY_MAPPER.apply(id));
    }
}
