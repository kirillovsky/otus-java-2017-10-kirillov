package ru.otus.kirillov.cacheengine.eviction.commands.size;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.eviction.commands.EvictionCommand;

/** Абстрактный класс для комманд вытеснения данных из кеша
 * по превышению допустимого размера кеша
 * Created by Александр on 07.02.2018.
 */
public abstract class MaxCacheSizeEvictionCommand extends EvictionCommand{

    private int maxCacheSize;

    public MaxCacheSizeEvictionCommand(Cache<?, ?> cache, int maxCacheSize) {
        super(cache);
        this.maxCacheSize = maxCacheSize;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }
}
