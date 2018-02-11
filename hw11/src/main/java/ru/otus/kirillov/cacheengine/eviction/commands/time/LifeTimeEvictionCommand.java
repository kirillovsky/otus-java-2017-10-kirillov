package ru.otus.kirillov.cacheengine.eviction.commands.time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.cache.CacheElement;

import java.time.Duration;

/** Команда вытеснения данных из кеша по времени их создания
 * Created by Александр on 06.02.2018.
 */
public class LifeTimeEvictionCommand extends TimeEvictionCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    public LifeTimeEvictionCommand(Cache<?, ?> cache, Duration duration) {
        super(cache, duration);
    }

    @Override
    public void execute() {
        LOGGER.info("Start to eviction by creation time. With params duration={} sec.", getDuration().getSeconds());
        int evictedElementsCount = evict(getDuration(), CacheElement::getCreationTime);
        LOGGER.info("Success!!! Evicted {} elements", evictedElementsCount);
    }
}
