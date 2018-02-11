package ru.otus.kirillov.cacheengine.eviction.commands.time;

import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.cache.CacheElement;
import ru.otus.kirillov.cacheengine.eviction.commands.EvictionCommand;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;

/** Базовый класс для простейших комманд вытеснения по временной политике
 * Created by Александр on 06.02.2018.
 */
public abstract class TimeEvictionCommand extends EvictionCommand {

    /**
     * Интервал времени, для дальнейшей проверки данных в {@link CacheElement}
     */
    private Duration duration;

    public TimeEvictionCommand(Cache<?, ?> cache, Duration duration) {
        super(cache);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Вытеснение данных из кэша по временной политике.
     * Если LocalDateTime.now() - {@param timeProvider.get()} >= {@param duration}
     * для элемента кэша, то данный элемент будет выкинут из кэша
     * @param duration - интревал времени
     * @param timeProvider - доступ к анализируемому времени элемента кэша
     * @return
     */
    protected int evict(Duration duration, Function<CacheElement, LocalDateTime> timeProvider) {
        CommonUtils.requiredNotNull(duration, timeProvider);
        Cache<?, ?> cache = getCache();
        return (int) cache.entrySet().stream()
                .filter(e -> isMoreFromTimePassedThanDuration(timeProvider.apply(e.getValue()), duration))
                .map(e -> ((Cache) cache).remove(e.getKey()))
                .count();
    }

    private boolean isMoreFromTimePassedThanDuration(LocalDateTime time, Duration duration) {
        return Duration.between(time, LocalDateTime.now())
                .compareTo(duration) > 0;
    }
}
