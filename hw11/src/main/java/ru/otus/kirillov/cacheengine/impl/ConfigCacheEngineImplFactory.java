package ru.otus.kirillov.cacheengine.impl;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.CacheEngineFactory;
import ru.otus.kirillov.cacheengine.cache.Caches;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys;
import ru.otus.kirillov.cacheengine.eviction.EvictionExecutors;
import ru.otus.kirillov.cacheengine.eviction.executor.EvictionExecutor;
import ru.otus.kirillov.cacheengine.eviction.executor.ScheduledCommandExecutor;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/** Фабрика для создания всей системы (кэш-энджин)
 * Created by Александр on 08.02.2018.
 */
public class ConfigCacheEngineImplFactory implements CacheEngineFactory {

    private CacheEngineConfig config;

    public ConfigCacheEngineImplFactory(CacheEngineConfig config) {
        this.config = CommonUtils.returnIfNotNull(config);
    }


    @Override
    public <K, V> CacheEngine<K, V> create() {
        Cache<K, V> cache = Caches.create(config.getOrDefault(CacheEngineConfigKeys.CACHE_TYPE), config);
        ScheduledCommandExecutor scheduledCommandExecutor =
                EvictionExecutors.createShedulerCommandExecutor(cache, config);
        EvictionExecutor overflowEvictionExecutor =
                EvictionExecutors.createOverflowEvictionExecutor(cache, config);
        return new CacheEngineImpl<>(cache, scheduledCommandExecutor, overflowEvictionExecutor);
    }
}
