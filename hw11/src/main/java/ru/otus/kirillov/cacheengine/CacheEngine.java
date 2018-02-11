package ru.otus.kirillov.cacheengine;

import ru.otus.kirillov.cacheengine.stats.CacheStatistics;

/**
 * Created by Александр on 04.02.2018.
 */
public interface CacheEngine<K, V> {

    void put(K key, V value);

    V get(K key);

    void delete(K key);

    CacheStatistics getStats();

    void dispose();

}
