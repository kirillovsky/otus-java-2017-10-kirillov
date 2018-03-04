package ru.otus.kirillov.model.commands.dbOperationStub;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DbWorkSimulatorTest {

    private static CacheEngine<String, DataSet> cacheEngine;
    private static DBService cachedProxyDbService;
    private DbWorkSimulator simulator;


    @BeforeClass
    public static void init() {
        cacheEngine = new ConfigCacheEngineImplFactory(new CacheEngineConfig())
                .create();
        cachedProxyDbService = new CachedProxyDBServiceFactory(
                new HibernateDBServiceFactory(), cacheEngine
        ).createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:test")
        );
    }

    @Before
    public void initSimulator() {
        simulator = new DbWorkSimulator(cachedProxyDbService);
    }

    @Test
    public void testCacheStatsChanged() throws InterruptedException {
        CacheStatistics oldStats = cacheEngine.getStats();
        Thread.sleep((long) (DbWorkSimulator.DELAY_TIME_IN_MS * 2.5));
        CacheStatistics newStats = cacheEngine.getStats();

        assertTrue(oldStats.getCacheHit() != newStats.getCacheHit() ||
                oldStats.getCacheMiss() != newStats.getCacheMiss() ||
                oldStats.getCacheSize() != newStats.getCacheSize()
        );
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[5][0]);
    }
}