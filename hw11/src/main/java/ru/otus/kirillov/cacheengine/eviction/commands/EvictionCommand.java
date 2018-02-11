package ru.otus.kirillov.cacheengine.eviction.commands;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/** Абстрактная комманда для вытеснения данных из кэша
 * Created by Александр on 07.02.2018.
 */
public abstract class EvictionCommand {

    private Cache<?, ?> cache;

    public EvictionCommand(Cache<?, ?> cache) {
        this.cache = CommonUtils.returnIfNotNull(cache);
    }

    public Cache<?, ?> getCache() {
        return cache;
    }

    public abstract void execute();
}
