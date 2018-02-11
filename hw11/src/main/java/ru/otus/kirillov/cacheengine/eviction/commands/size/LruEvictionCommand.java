package ru.otus.kirillov.cacheengine.eviction.commands.size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.Cache;

import java.util.Comparator;

/**
 * Реализация политики LRU для вытеснения данных из кэша
 * <p>
 * Created by Александр on 07.02.2018.
 */
public class LruEvictionCommand extends MaxCacheSizeEvictionCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    private double reductionFactor;

    public LruEvictionCommand(Cache<?, ?> cache, int maxCacheSize, double reductionFactor) {
        super(cache, maxCacheSize);
        this.reductionFactor = reductionFactor;
    }

    @Override
    public void execute() {
        int currentCacheSize = getCache().size();
        int maxCacheSize = getMaxCacheSize();

        if (maxCacheSize > currentCacheSize) {
            LOGGER.debug("New cache size {} must be less than current cache size {}",
                    maxCacheSize, currentCacheSize);
            return;
        }

        int newCacheSize = (int) (currentCacheSize * reductionFactor);

        if (newCacheSize < 0 || newCacheSize >= currentCacheSize) {
            throw new IllegalStateException(
                    String.format("Wrong new cache size = %d, old cache size = %d", newCacheSize, currentCacheSize)
            );
        }

        LOGGER.info("Start to eviction by LRU-policy. With params max cache size = {}, " +
                        "current cache size = {}, reduction factor = {}, new cache size = {}", maxCacheSize, currentCacheSize,
                reductionFactor, newCacheSize);

        getCache().entrySet().stream()
                .sorted(Comparator.comparing(p -> p.getValue().getLastAccessTime()))
                .limit(currentCacheSize - newCacheSize)
                .forEach(p -> ((Cache) getCache()).remove(p.getKey()));
        LOGGER.info("Eviction success!");
    }
}
