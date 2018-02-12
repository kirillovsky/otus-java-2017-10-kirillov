package ru.otus.kirillov.cacheengine;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.cacheengine.cache.CacheElement;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** ИНтерфейс для хранения элементов кэша.
 * Собственно сам кэш, очень похож на мапу
 * Created by Александр on 05.02.2018.
 */
public interface Cache<K, V> {

    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    Optional<CacheElement<V>> get(K key);

    CacheElement<V> put(K key, CacheElement<V> value);

    CacheElement<V> remove(K key);

    void putAll(Map<? extends K, ? extends CacheElement<V>> m);

    void clear();

    Set<K> keySet();

    Collection<CacheElement<V>> values();

    Set<Pair<K, CacheElement<V>>> entrySet();
}
