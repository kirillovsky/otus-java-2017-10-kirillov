package ru.otus.kirillov.model.service.getCacheStats.messages;

import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsResult;

public final class GetCacheStatsRs {
    private final long cacheSize;
    private final long cacheHit;
    private final long cacheMiss;

    public GetCacheStatsRs(GetCacheStatsResult rs) {
        cacheHit = rs.getCacheHit();
        cacheSize = rs.getCacheSize();
        cacheMiss = rs.getCacheMiss();
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }
}
