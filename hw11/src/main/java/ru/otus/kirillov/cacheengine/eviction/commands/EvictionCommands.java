package ru.otus.kirillov.cacheengine.eviction.commands;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys;
import ru.otus.kirillov.cacheengine.config.EvictionType;
import ru.otus.kirillov.cacheengine.eviction.commands.size.LruEvictionCommand;
import ru.otus.kirillov.cacheengine.eviction.commands.time.LastAccessTimeEvictionCommand;
import ru.otus.kirillov.cacheengine.eviction.commands.time.LifeTimeEvictionCommand;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/** Создание комманд вытеснения данных из кэша
 * Created by Александр on 11.02.2018.
 */
public final class EvictionCommands {

    private EvictionCommands() {
    }

    public static EvictionCommand create(EvictionType type, Cache<?, ?> cache, CacheEngineConfig config) {
        CommonUtils.requiredNotNull(type, cache, config);
        switch (type) {
            case LRU:
                return createLRU(cache, config);
            case LAST_ACCESS_TIME_EVICTION:
                return createLastAccessTime(cache, config);
            case LIFE_TIME_EVICTION:
                return createLifeTime(cache, config);
            default:
                throw new IllegalArgumentException("Unknown command type - " + type);
        }
    }

    public static EvictionCommand createLRU(Cache<?, ?> cache, CacheEngineConfig config) {
        CommonUtils.requiredNotNull(cache, config);
        int maxCacheSize = config.getOrDefault(CacheEngineConfigKeys.MAX_CACHE_SIZE);
        double reductionFactor = config.getOrDefault(CacheEngineConfigKeys.LRU_REDUCTION_FACTOR);
        return new LruEvictionCommand(cache, maxCacheSize, reductionFactor);
    }

    public static EvictionCommand createLifeTime(Cache<?, ?> cache, CacheEngineConfig config) {
        CommonUtils.requiredNotNull(cache, config);
        return new LifeTimeEvictionCommand(cache, config.getOrDefault(CacheEngineConfigKeys.LIFE_TIME_DURATION));
    }

    public static EvictionCommand createLastAccessTime(Cache<?, ?> cache, CacheEngineConfig config) {
        CommonUtils.requiredNotNull(cache, config);
        return new LastAccessTimeEvictionCommand(cache,
                config.getOrDefault(CacheEngineConfigKeys.LAST_ACCESS_TIME_DURATION));
    }
}
