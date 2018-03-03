package ru.otus.kirillov.model.commands.getCacheStats;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Результат запроса на вывод состояния кэша
 */
public final class GetCacheStatsResult implements Result {

    private final long cacheHit;

    private  final long cacheMiss;

    private final long cacheSize;

    private GetCacheStatsResult(long cacheHit, long cacheMiss, long cacheSize) {
        this.cacheHit = cacheHit;
        this.cacheMiss = cacheMiss;
        this.cacheSize = cacheSize;
    }

    public static GetCacheStatsResult of(CacheStatistics stats) {
        CommonUtils.requiredNotNull(stats);
        return new GetCacheStatsResult(stats.getCacheHit(), stats.getCacheMiss(), stats.getCacheSize());
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cacheHit", cacheHit)
                .append("cacheMiss", cacheMiss)
                .append("cacheSize", cacheSize)
                .toString();
    }
}
