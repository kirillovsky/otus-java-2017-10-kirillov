package ru.otus.kirillov.cacheengine.cache;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheType;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/** Текущие имплементации кэшей
 * Created by Александр on 11.02.2018.
 */
public final class Caches {

    private Caches() {
    }

    public static <K, V> Cache<K, V> create(CacheType type, CacheEngineConfig config) {
        CommonUtils.requiredNotNull(type, config);

        switch (type) {
            case SOFT_REFERENCE:
                return createSoftReferenceCache(config);
            default:
                throw new IllegalArgumentException("Unknown cache type - " + type);
        }
    }

    public static <K, V> Cache<K, V> createSoftReferenceCache(CacheEngineConfig config) {
        return new SoftReferenceCache<>();
    }
}
