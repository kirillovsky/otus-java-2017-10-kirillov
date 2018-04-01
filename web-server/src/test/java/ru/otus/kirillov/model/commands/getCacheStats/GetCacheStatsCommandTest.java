package ru.otus.kirillov.model.commands.getCacheStats;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetCacheStatsCommandTest {

    private GetCacheStatsCommand command;

    private CacheEngine cacheEngineMock;

    @Before
    public void setUp() {
        cacheEngineMock = mock(CacheEngine.class);
        command = new GetCacheStatsCommand(cacheEngineMock);
    }

    private static final long CACHE_HIT_RESULT = 100;
    private static final long CACHE_MISS_RESULT = 200;
    private static final int CACHE_SIZE_RESULT = 300;

    @Test
    public void testSuccess() {
        when(cacheEngineMock.getStats()).thenReturn(
                CacheStatistics.of(CACHE_HIT_RESULT, CACHE_MISS_RESULT, CACHE_SIZE_RESULT)
        );
        GetCacheStatsModelResult cacheStatsResult =
                (GetCacheStatsModelResult) command.execute(new GetCacheStatsModelRequest());

        assertEquals(CACHE_HIT_RESULT, cacheStatsResult.getCacheHit());
        assertEquals(CACHE_MISS_RESULT, cacheStatsResult.getCacheMiss());
        assertEquals(CACHE_SIZE_RESULT, cacheStatsResult.getCacheSize());
    }

    private static final String ERRONEOUS_MESSAGE = "#1%^TEZZZ#1%^";

    @Test
    public void testError() {
        when(cacheEngineMock.getStats()).thenThrow(
                new UnsupportedOperationException(ERRONEOUS_MESSAGE)
        );

        ErroneousModelResult erroneousResult = (ErroneousModelResult) command.execute(new GetCacheStatsModelRequest());
        assertEquals(ERRONEOUS_MESSAGE, erroneousResult.getCause());
    }
}