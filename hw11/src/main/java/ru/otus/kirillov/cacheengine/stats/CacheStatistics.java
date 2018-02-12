package ru.otus.kirillov.cacheengine.stats;

/** Статистика по использованию кэша.
 * Created by Александр on 05.02.2018.
 */
public class CacheStatistics {

    private long cacheHit;

    private long cacheMiss;

    private int cacheSize;

    public static CacheStatistics of(long cacheHit, long cacheMiss, int cacheSize) {
        return new CacheStatistics(cacheHit, cacheMiss, cacheSize);
    }

    protected CacheStatistics(long cacheHit, long cacheMiss, int cacheSize) {
        this.cacheHit = cacheHit;
        this.cacheMiss = cacheMiss;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }

    public int getCacheSize() {
        return cacheSize;
    }
}
