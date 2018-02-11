package ru.otus.kirillov.cacheengine.eviction.executor;

/** Исполнитель комманд "по расписанию"
 * Created by Александр on 08.02.2018.
 */
public interface ScheduledCommandExecutor extends EvictionExecutor {

    void start();

    void stop();
}
