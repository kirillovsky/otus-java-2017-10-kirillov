package ru.otus.kirillov.cacheengine.eviction;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.cacheengine.Cache;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys;
import ru.otus.kirillov.cacheengine.config.EvictionType;
import ru.otus.kirillov.cacheengine.eviction.commands.EvictionCommands;
import ru.otus.kirillov.cacheengine.eviction.executor.EvictionExecutor;
import ru.otus.kirillov.cacheengine.eviction.executor.MultipleScheduledEvictionExecutor;
import ru.otus.kirillov.cacheengine.eviction.executor.ScheduledCommandExecutor;
import ru.otus.kirillov.cacheengine.eviction.executor.SingleEvictionExecutor;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.util.stream.Collectors;

/**
 * Created by Александр on 11.02.2018.
 */
public final class EvictionExecutors {

    private EvictionExecutors() {
    }

    public static ScheduledCommandExecutor createShedulerCommandExecutor(Cache<?, ?> cache,
                                                                         CacheEngineConfig config) {
        CommonUtils.requiredNotNull(config);
        return new MultipleScheduledEvictionExecutor(
             config.getOrDefault(CacheEngineConfigKeys.SCHEDULED_EVICTIONS).stream()
                .map(p -> Pair.of(EvictionCommands.create(p.getLeft(), cache, config), p.getRight()))
                .collect(Collectors.toList())
        );
    }

    public static EvictionExecutor createOverflowEvictionExecutor(Cache<?, ?> cache,
                                                                  CacheEngineConfig config) {
        CommonUtils.requiredNotNull(cache, config);
        EvictionType overflowEvictionCommandType = config.getOrDefault(CacheEngineConfigKeys.CACHE_OVERFLOW_EVICTION);
        return new SingleEvictionExecutor(
                EvictionCommands.create(overflowEvictionCommandType, cache, config)
        );
    }
}
