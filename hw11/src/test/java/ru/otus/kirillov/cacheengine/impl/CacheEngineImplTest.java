package ru.otus.kirillov.cacheengine.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.CacheEngineFactory;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys;
import ru.otus.kirillov.cacheengine.config.EvictionType;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by Александр on 11.02.2018.
 */
public class CacheEngineImplTest {

    private CacheEngineFactory cacheEngineFactory;
    private CacheEngine<Integer, String> engine;
    private Function<Integer, String> keyToValueMapper = Objects::toString;

    @Test
    public void testPut() throws Exception {
        createDefaultCacheEngine();
        int[] testInts = IntStream.range(0, 1_000).toArray();

        Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
        Arrays.stream(testInts).forEach(i -> Assert.assertEquals(keyToValueMapper.apply(i), engine.get(i)));

        Assert.assertEquals(1_000, engine.getStats().getCacheHit());
        Assert.assertEquals(0, engine.getStats().getCacheMiss());
    }

    @Test
    public void testGet() throws Exception {
        createDefaultCacheEngine();

        Assert.assertNull(engine.get(1_000));
        int[] testInts = IntStream.range(-500, 500).toArray();

        Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
        Arrays.stream(testInts).forEach(i -> Assert.assertEquals(keyToValueMapper.apply(i), engine.get(i)));
        Assert.assertNull(engine.get(501));
        Assert.assertNull(engine.get(-501));
        Assert.assertNotNull(engine.get(100));

        Assert.assertEquals(1_001, engine.getStats().getCacheHit());
        Assert.assertEquals(3, engine.getStats().getCacheMiss());
    }


    @Test
    public void testEvictionByMaxCacheSizeNonZeroReductionFactor() throws Exception {
        CacheEngineConfig config = getDefaultConfig();
        config.with(CacheEngineConfigKeys.MAX_CACHE_SIZE, 1_000)
                //Оставляем 1 элемент
                .with(CacheEngineConfigKeys.LRU_REDUCTION_FACTOR, 1.0 / 1_000);
        engine = new ConfigCacheEngineImplFactory(config).create();

        //Добавляем max cache size + 1 элементов
        int[] testInts = IntStream.range(-500, 501).toArray();
        Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
        Arrays.stream(testInts).forEach(i -> engine.get(i));

        Assert.assertEquals(2, engine.getStats().getCacheHit());
        Assert.assertEquals(999, engine.getStats().getCacheMiss());
    }

    @Test
    public void testEvictionByMaxCacheSizeZeroReductionFactor() throws Exception {
        CacheEngineConfig config = getDefaultConfig();
        config.with(CacheEngineConfigKeys.MAX_CACHE_SIZE, 1_000)
                //Оставляем 1 элемент
                .with(CacheEngineConfigKeys.LRU_REDUCTION_FACTOR, 0.);
        engine = new ConfigCacheEngineImplFactory(config).create();

        //Добавляем max cache size + 1 элементов
        int[] testInts = IntStream.range(-500, 501).toArray();
        Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
        Arrays.stream(testInts).forEach(i -> engine.get(i));

        Assert.assertEquals(1, engine.getStats().getCacheHit());
        Assert.assertEquals(1_000, engine.getStats().getCacheMiss());
    }

    @Test
    public void testLifeTimeEviction() throws Exception {
        CacheEngineConfig config = getDefaultConfig()
                //Эвикшн сработает через 2 секунды от старта и удалить все элементы, со сроком жизни > 1 сек
                .with(CacheEngineConfigKeys.SCHEDULED_EVICTIONS,
                        Collections.singletonList(Pair.of(EvictionType.LIFE_TIME_EVICTION, Duration.ofSeconds(2))))
                .with(CacheEngineConfigKeys.LIFE_TIME_DURATION, Duration.ofSeconds(1));

        engine = new ConfigCacheEngineImplFactory(config).create();
        int[] testInts = IntStream.range(-500, 1000).toArray();
        for(int count = 1; count < 4; count++) {
            int testedInt = count * -1_000;
            Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
            //уснули на 3 секунды для гарантированного срабатывания эвикшена
            Thread.sleep(3 * 1_000);

            engine.put(testedInt, keyToValueMapper.apply(testedInt));
            Arrays.stream(testInts).forEach(i -> Assert.assertNull(engine.get(i)));
            Assert.assertEquals(keyToValueMapper.apply(testedInt), engine.get(testedInt));
        }

        Assert.assertEquals(3, engine.getStats().getCacheHit());
        Assert.assertEquals(3 * 1_500, engine.getStats().getCacheMiss());
    }

    @Test
    public void testLastAccessTimeEviction() throws Exception {
        CacheEngineConfig config = getDefaultConfig()
                //Эвикшн сработает через 2 секунды от старта и удалить все элементы, со сроком жизни > 1 сек
                .with(CacheEngineConfigKeys.SCHEDULED_EVICTIONS,
                        Collections.singletonList(Pair.of(EvictionType.LAST_ACCESS_TIME_EVICTION, Duration.ofSeconds(1))))
                .with(CacheEngineConfigKeys.LAST_ACCESS_TIME_DURATION, Duration.ofSeconds(2));

        int testedInt = -999;
        engine = new ConfigCacheEngineImplFactory(config).create();
        engine.put(testedInt, keyToValueMapper.apply(testedInt));
        int[] testInts = IntStream.range(-500, 1000).toArray();

        for(int count = 1; count < 4; count++) {
            Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
            //уснули на 3 секунды для гарантированного срабатывания эвикшена
            Thread.sleep(1_500);
            Assert.assertEquals(keyToValueMapper.apply(testedInt), engine.get(testedInt));
            Thread.sleep(1_500);
            Arrays.stream(testInts).forEach(i -> Assert.assertNull(engine.get(i)));
            Assert.assertEquals(keyToValueMapper.apply(testedInt), engine.get(testedInt));
        }

        Assert.assertEquals(6, engine.getStats().getCacheHit());
        Assert.assertEquals(3 * 1_500, engine.getStats().getCacheMiss());
    }

    @Test
    public void testDispose() throws Exception {
        CacheEngineConfig config = getDefaultConfig()
                //Эвикшн сработает через 2 секунды от старта и удалить все элементы, со сроком жизни > 1 сек
                .with(CacheEngineConfigKeys.SCHEDULED_EVICTIONS,
                        Arrays.asList(
                                Pair.of(EvictionType.LIFE_TIME_EVICTION, Duration.ofSeconds(2)),
                                Pair.of(EvictionType.LAST_ACCESS_TIME_EVICTION, Duration.ofSeconds(1)
                        )))
                .with(CacheEngineConfigKeys.LIFE_TIME_DURATION, Duration.ofSeconds(1))
                .with(CacheEngineConfigKeys.LAST_ACCESS_TIME_DURATION, Duration.ofSeconds(2));

        int testedInt = -999;
        engine = new ConfigCacheEngineImplFactory(config).create();
        int[] testInts = IntStream.range(-500, 1000).toArray();
        for(int count = 1; count < 4; count++) {
            Arrays.stream(testInts).forEach(i -> engine.put(i, keyToValueMapper.apply(i)));
            //уснули на 3 секунды для гарантированного срабатывания эвикшена
            Thread.sleep(1_500);
            engine.put(testedInt, keyToValueMapper.apply(testedInt));
            Thread.sleep(1_500);
            Arrays.stream(testInts).forEach(i -> engine.get(i));
            Assert.assertEquals(keyToValueMapper.apply(testedInt), engine.get(testedInt));
            //Отключаем все таймеры
            engine.dispose();
        }

        Assert.assertEquals(1 + 2 *(1 + 1_500), engine.getStats().getCacheHit());
        Assert.assertEquals(1_500, engine.getStats().getCacheMiss());
    }

    @After
    public void after() {
        if (engine != null) {
            engine.dispose();
        }
    }

    private CacheEngineConfig getDefaultConfig() {
        return new CacheEngineConfig();
    }

    private void createDefaultCacheEngine() {
        cacheEngineFactory = new ConfigCacheEngineImplFactory(getDefaultConfig());
        engine = cacheEngineFactory.create();
    }
}