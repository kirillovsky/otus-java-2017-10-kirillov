package ru.otus.kirillov.cacheengine.cache;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация кэша на софт-референсах.
 * Отличительная черта - ограничение на то,
 * что в качестве занчений не могут быть null.
 * Ни SoftReference<CacheElement<V>>, ни CacheElement<V> в нем.
 * Если CacheElement<V> - то от таких значений надо избавляться!
 * CacheElement<V> может быть = null, только при нехватке памяти в хипе
 * и удаления значение SoftReference сборщиком мусора. Такие entry надо очищать.
 * Это будет происходить в методах size(), isEmpty(), keySet(), values(),
 * entrySet().
 * <p>
 * Created by Александр on 05.02.2018.
 */
public class SoftReferenceCache<K, V> implements Cache<K, V> {

    private Map<K, SoftReference<CacheElement<V>>> inner = new LinkedHashMap<>();

    @Override
    public int size() {
        removeAddledSoftReference();
        return inner.size();
    }

    @Override
    public boolean isEmpty() {
        removeAddledSoftReference();
        return inner.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return inner.containsKey(key) && get(key) != null;
    }

    @Override
    public Optional<CacheElement<V>> get(K key) {
        return Optional.ofNullable(inner.get(key))
                .map(SoftReference::get);
    }

    @Override
    public CacheElement<V> put(K key, CacheElement<V> value) {
        CommonUtils.requiredNotNull(value);
        inner.put(key, new SoftReference<>(value));
        return value;
    }

    @Override
    public CacheElement<V> remove(K key) {
        return inner.remove(key).get();
    }

    @Override
    public void putAll(Map<? extends K, ? extends CacheElement<V>> m) {
        CommonUtils.requiredNotNull(m);
        m.entrySet().stream()
                .forEach(e -> put(e.getKey(), e.getValue()));
    }

    @Override
    public void clear() {
        inner.clear();
    }

    @Override
    public Set<K> keySet() {
        removeAddledSoftReference();
        return inner.keySet();
    }

    @Override
    public Collection<CacheElement<V>> values() {
        removeAddledSoftReference();
        return inner.values().stream()
                .map(SoftReference::get)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Pair<K, CacheElement<V>>> entrySet() {
        removeAddledSoftReference();
        return inner.entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue().get()))
                .collect(Collectors.toSet());
    }

    /**
     * Удаление протухших SoftReferences
     */
    public void removeAddledSoftReference() {
        inner.entrySet().stream()
                .filter(e -> e.getValue().get() == null)
                .forEach(e -> remove(e.getKey()));
    }
}
