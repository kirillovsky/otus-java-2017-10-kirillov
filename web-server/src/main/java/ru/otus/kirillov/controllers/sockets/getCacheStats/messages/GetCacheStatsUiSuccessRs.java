package ru.otus.kirillov.controllers.sockets.getCacheStats.messages;

import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.utils.CommonUtils;

public class GetCacheStatsUiSuccessRs {

    private final String cacheSize;
    private final String cacheHit;
    private final String cacheMiss;

    private GetCacheStatsUiSuccessRs(String cacheSize, String cacheHit, String cacheMiss) {
        this.cacheSize = CommonUtils.retunIfNotNull(cacheSize);
        this.cacheHit = CommonUtils.retunIfNotNull(cacheHit);
        this.cacheMiss = CommonUtils.retunIfNotNull(cacheMiss);
    }

    public static GetCacheStatsUiSuccessRs of(String cacheSize, String cacheHit, String cacheMiss) {
        return new GetCacheStatsUiSuccessRs(cacheSize, cacheHit, cacheMiss);
    }

    public static GetCacheStatsUiSuccessRs of(CacheStatistics stats) {
        CommonUtils.requiredNotNull(stats);
        return new GetCacheStatsUiSuccessRs(
                Integer.toString(stats.getCacheSize()),
                Long.toString(stats.getCacheHit()),
                Long.toString(stats.getCacheMiss())
        );
    }

    public String getCacheSize() {
        return cacheSize;
    }

    public String getCacheHit() {
        return cacheHit;
    }

    public String getCacheMiss() {
        return cacheMiss;
    }
}
