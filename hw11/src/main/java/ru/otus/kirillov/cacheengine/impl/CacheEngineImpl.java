package ru.otus.kirillov.cacheengine.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.cache.CacheElement;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.eviction.executor.EvictionExecutor;
import ru.otus.kirillov.cacheengine.eviction.executor.ScheduledCommandExecutor;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/**
 * Имплементация кэш-энджина
 * Created by Александр on 05.02.2018.
 */
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Cache<K, V> cache;
    private final ScheduledCommandExecutor scheduledExecutor;
    private final EvictionExecutor maxCacheSizeEvictionExecutor;

    private long hit = 0;
    private long miss = 0;

    public CacheEngineImpl(Cache<K, V> cache, ScheduledCommandExecutor scheduledExecutor,
                           EvictionExecutor maxCacheSizeEvictionExecutor) {
        this.cache = CommonUtils.returnIfNotNull(cache);
        this.scheduledExecutor = CommonUtils.returnIfNotNull(scheduledExecutor);
        this.maxCacheSizeEvictionExecutor = CommonUtils.returnIfNotNull(maxCacheSizeEvictionExecutor);
        this.scheduledExecutor.start();
    }

    @Override
    public void put(K key, V value) {
        ensureCacheSize();
        cache.put(key, CacheElement.of(value));
        LOGGER.debug("(Key={}, Value={}) successfully putted into cache", key, value);
    }

    @Override
    public V get(K key) {
        V result = updateStats(cache.get(key)
                .map(CacheElement::getValue)
                .orElse(null)
        );
        LOGGER.debug("Get (Key={}, Value={}) from cache", key, result);
        return result;
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
        LOGGER.debug("Delete (Key={}) from cache", key);
    }

    @Override
    public CacheStatistics getStats() {
        return CacheStatistics.of(hit, miss);
    }

    @Override
    public void dispose() {
        scheduledExecutor.stop();
    }

    private void ensureCacheSize() {
        maxCacheSizeEvictionExecutor.execute();
    }

    private V updateStats(V value) {
        if (value == null) {
            miss++;
        } else {
            hit++;
        }
        return value;
    }
}
