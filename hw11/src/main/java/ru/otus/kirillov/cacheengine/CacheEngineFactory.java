package ru.otus.kirillov.cacheengine;

/** Фабрика имплементаций {@link CacheEngine}
 * Created by Александр on 08.02.2018.
 */
public interface CacheEngineFactory {

    <K, V> CacheEngine<K, V> create();
}
