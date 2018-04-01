package ru.otus.kirillov.model.services.dbOperationStub;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.*;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class DbWorkSimulatorTest {

    private static CacheEngine<String, DataSet> cacheEngine;
    private static DBService cachedProxyDbService;
    private static DbWorkSimulator simulator;


    @BeforeClass
    public static void init() {
        cacheEngine = new ConfigCacheEngineImplFactory(
                new CacheEngineConfig().with(CacheEngineConfigKeys.SCHEDULED_EVICTIONS, Collections.emptyList())
        ).create();

        cachedProxyDbService = new CachedProxyDBServiceFactory(
                new HibernateDBServiceFactory(), cacheEngine
        ).createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:DbWorkSimulatorTest")
        );
        simulator = new DbWorkSimulator(cachedProxyDbService);
        simulator.initDbEntities();
    }

    @Test
    public void testCacheStatsAfterRead() {
        CacheStatistics oldStats = cacheEngine.getStats();
        simulator.randomRead();
        CacheStatistics newStats = cacheEngine.getStats();
        compareCacheStats(oldStats, newStats);
    }

    @Test
    public void testCacheStatsAfterSave() {
        CacheStatistics oldStats = cacheEngine.getStats();
        simulator.randomSave();
        CacheStatistics newStats = cacheEngine.getStats();
        compareCacheStats(oldStats, newStats);
    }

    @Test
    public void testCacheStatsAfterUpdate() {
        CacheStatistics oldStats = cacheEngine.getStats();
        simulator.randomUpdate();
        CacheStatistics newStats = cacheEngine.getStats();
        compareCacheStats(oldStats, newStats);
    }

    public void compareCacheStats(CacheStatistics oldStats, CacheStatistics newStats) {
        assertTrue(String.format("Equals old stats-%s,\n new stats - %s", toStr(oldStats), toStr(newStats)),
                oldStats.getCacheHit() != newStats.getCacheHit() ||
                        oldStats.getCacheMiss() != newStats.getCacheMiss() ||
                        oldStats.getCacheSize() != newStats.getCacheSize()
        );
    }

    private String toStr(CacheStatistics stats) {
        return new ToStringBuilder(this)
                .append("hit", stats.getCacheHit())
                .append("miss", stats.getCacheMiss())
                .append("miss", stats.getCacheSize())
                .toString();
    }
}