package ru.otus.kirillov.cacheengine.eviction.executor;

/** Интерфейс для вызова исполняемых комманда для вытеснения данных из кеша
 * Created by Александр on 08.02.2018.
 */
public interface EvictionExecutor {

    void execute();
}
