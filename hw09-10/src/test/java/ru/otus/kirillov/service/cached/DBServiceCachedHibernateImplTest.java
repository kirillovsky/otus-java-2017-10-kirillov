package ru.otus.kirillov.service.cached;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceTest;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;

/**
 * Created by Александр on 11.02.2018.
 */
public class DBServiceCachedHibernateImplTest extends DBServiceTest {

    protected static CacheEngine<String, DataSet> cacheEngine;
    protected static DBService cachedProxyDbService;

    @BeforeClass
    public static void init() {
        cacheEngine = new ConfigCacheEngineImplFactory(
                //Дефолтная конфигурация кэша. См. дефолтные знaчения ключей в CacheEngineConfigKeys
                new CacheEngineConfig()
        ).create();
        cachedProxyDbService = new CachedProxyDBServiceFactory(
                new HibernateDBServiceFactory(), cacheEngine
        ).createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:test")
        );

    }

    @Override
    protected DBService getDbService() {
        return cachedProxyDbService;
    }

    @After
    public void clearDB() {
        super.clearDB();
        cacheEngine.reset();
        checkCacheStats(0, 0, 0);
    }

    @Test
    public void emptyUserReadTest() {
        super.emptyUserReadTest();
        checkCacheStats(0, 0, 0);
    }

    @Test
    public void readOneSimpleUser() {
        super.readOneSimpleUser();
        checkCacheStats(1, 0, 1);
    }

    @Test
    public void readOneFillUser() {
        super.readOneFillUser();
        checkCacheStats(1, 0, 4);
    }

    @Test
    public void readOneFillUserSeparatelySave() {
        super.readOneFillUserSeparatelySave();
        checkCacheStats(1, 0, 4);
    }

    @Test
    public void updateSimpleFields() {
        super.updateSimpleFields();
        checkCacheStats(1, 0, 1);
    }

    @Test
    public void updateDependentDataSets() {
        super.updateDependentDataSets();
        checkCacheStats(1, 0, 1);
    }

    @Test
    public void updateExtendDependentDataSets() {
        super.updateExtendDependentDataSets();
        checkCacheStats(1, 0, 1);
    }

    @Test
    public void updateReduceOneToManyFields() {
        super.updateReduceOneToManyFields();
        checkCacheStats(1, 0, 1);
    }

    @Test
    public void deleteSimpleDataSets() {
        super.deleteSimpleDataSets();
        checkCacheStats(2, 0, 0);
    }

    @Test
    public void deleteUserWithoutDependentEntities() {
        super.deleteUserWithoutDependentEntities();
        checkCacheStats(1, 0, 0);
    }

    private void checkCacheStats(long cacheHit, long cacheMiss, int cacheSize) {
        CacheStatistics stats = cacheEngine.getStats();
        Assert.assertEquals(cacheHit, stats.getCacheHit());
        Assert.assertEquals(cacheMiss, stats.getCacheMiss());
        Assert.assertEquals(cacheSize, stats.getCacheSize());
    }

}
