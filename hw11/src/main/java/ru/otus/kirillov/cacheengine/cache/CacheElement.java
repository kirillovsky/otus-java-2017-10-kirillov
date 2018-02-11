package ru.otus.kirillov.cacheengine.cache;

import java.time.LocalDateTime;

/** Элемент кэша
 * Created by Александр on 04.02.2018.
 */
public class CacheElement<V> {

    private V value;
    private final LocalDateTime creationTime = LocalDateTime.now();
    private LocalDateTime lastAccessTime = LocalDateTime.now();

    public static <V> CacheElement<V> of(V value) {
        return new CacheElement<>(value);
    }

    protected CacheElement(V value) {
        this.value = value;
    }

    public V getValue() {
        updateLastAccessTime();
        return value;
    }

    public void setValue(V value) {
        updateLastAccessTime();
        this.value = value;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    private void updateLastAccessTime() {
        lastAccessTime = LocalDateTime.now();
    }
}
