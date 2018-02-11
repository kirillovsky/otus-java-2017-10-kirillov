package ru.otus.kirillov.cacheengine.stats;

/** Статистика по использованию кэша.
 * Created by Александр on 05.02.2018.
 */
public class CacheStatistics {

    private long cacheHit;

    private long cacheMiss;

    public static CacheStatistics of(long cacheHit, long cacheMiss) {
        return new CacheStatistics(cacheHit, cacheMiss);
    }

    protected CacheStatistics(long cacheHit, long cacheMiss) {
        this.cacheHit = cacheHit;
        this.cacheMiss = cacheMiss;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }
}
