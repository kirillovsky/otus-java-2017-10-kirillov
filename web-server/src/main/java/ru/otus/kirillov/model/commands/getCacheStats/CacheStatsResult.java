package ru.otus.kirillov.model.commands.getCacheStats;

import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Результат запроса на вывод состояния кэша
 */
public final class CacheStatsResult implements Result {

    private final long cacheHit;

    private  final long cacheMiss;

    private final long cacheSize;

    private CacheStatsResult(long cacheHit, long cacheMiss, long cacheSize) {
        this.cacheHit = cacheHit;
        this.cacheMiss = cacheMiss;
        this.cacheSize = cacheSize;
    }

    public static CacheStatsResult of(CacheStatistics stats) {
        CommonUtils.requiredNotNull(stats);
        return new CacheStatsResult(stats.getCacheHit(), stats.getCacheMiss(), stats.getCacheSize());
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }

    public long getCacheSize() {
        return cacheSize;
    }
}
