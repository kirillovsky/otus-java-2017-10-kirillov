package ru.otus.kirillov.cacheengine.config;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Ключи для конфигурации кэша
 * Created by Александр on 11.02.2018.
 */
public final class CacheEngineConfigKeys {

    /**
     * Тип кэша под управление энджина. Пока он только один - Софт референс кэш
     */
    public static final CacheEngineConfigKey<CacheType> CACHE_TYPE =
            new CacheEngineConfigKey<>("CACHE_TYPE", CacheType.class, CacheType.SOFT_REFERENCE);

    /**
     * Максимальный размер кэша. Может понадобиться различным коммандам вытеснения
     */
    public static final CacheEngineConfigKey<Integer> MAX_CACHE_SIZE =
            new CacheEngineConfigKey<>("MAX_CACHE_SIZE", Integer.class, Integer.MAX_VALUE);

    /**
     * Тип эвикшена, применящийся при необходимости очитить переполненный кэш
     */
    public static final CacheEngineConfigKey<EvictionType> CACHE_OVERFLOW_EVICTION =
            new CacheEngineConfigKey<>("CACHE_OVERFLOW_EVICTIONS", EvictionType.class, EvictionType.LRU);

    //Использование TokenType для получение точного типа выглядит как грязный хак.
    // Но если вместо него указывать List.class вывод типов ломается
    /**
     * Список переодически запускаемых эвикшн-комманд, с указанием периодичности запуска
     */
    public static final CacheEngineConfigKey<List<Pair<EvictionType, Duration>>> SCHEDULED_EVICTIONS =
            new CacheEngineConfigKey<>("SCHEDULED_EVICTIONS",
                    new CommonUtils.TokenType<List<Pair<EvictionType, Duration>>>(){}.getType(),
                    Arrays.asList(
                            Pair.of(EvictionType.LAST_ACCESS_TIME_EVICTION, Duration.ofSeconds(2)),
                            Pair.of(EvictionType.LIFE_TIME_EVICTION, Duration.ofSeconds(2))
                    )
            );

    /**
     * Время жизни элемента кэша для комманды "Вытеснение по времени жизни"
     */
    public static final EvictionConfigKey<Duration> LIFE_TIME_DURATION =
            new EvictionConfigKey<>("LIFE_TIME_DURATION", Duration.class, Duration.ofSeconds(10), EvictionType.LIFE_TIME_EVICTION);

    /**
     * Время жизни элемента кэша для комманды "Вытеснение по времени последего обращения"
     */
    public static final EvictionConfigKey<Duration> LAST_ACCESS_TIME_DURATION =
            new EvictionConfigKey<>("LAST_ACCESS_TIME_DURATION", Duration.class, Duration.ofSeconds(5),
                    EvictionType.LAST_ACCESS_TIME_EVICTION);

    /**
     * Множитель для максимального размера кэша (0, 1) для комманды "Вытеснение давно неиспользуемых (LRU)"
     * Как эта штука работает. После достижения предела по размеру кэша и запуска данной комманды из кэша
     * удаляется (maxCacheSize - newCacheSize) элементов с самым ранним временем последнего обращения,
     * newCacheSize = maxCacheSize * reductionFactor
     */
    public static final EvictionConfigKey<Double> LRU_REDUCTION_FACTOR =
            new EvictionConfigKey<>("LRU_REDUCTION_FACTOR", Double.class, 0.85, EvictionType.LRU);


    private CacheEngineConfigKeys(){}

    private static class CacheEngineConfigKey<T> {
        private Class<T> paramType;
        private T defaultValue;
        private String uniqueKey;

        public CacheEngineConfigKey(String uniqueKey, Class<T> paramType, T defaultValue) {
            this.paramType = paramType;
            this.defaultValue = defaultValue;
            this.uniqueKey = uniqueKey;
        }

        public Class<T> getParamType() {
            return paramType;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheEngineConfigKey)) return false;
            CacheEngineConfigKey<?> that = (CacheEngineConfigKey<?>) o;
            return Objects.equals(uniqueKey, that.uniqueKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uniqueKey);
        }
    }

    private static class EvictionConfigKey<K> extends CacheEngineConfigKey<K> {
        private EvictionType evictionType;

        public EvictionConfigKey(String uniqueKey, Class<K> paramType, K defaultValue, EvictionType evictionType) {
            super(uniqueKey, paramType, defaultValue);
            this.evictionType = CommonUtils.returnIfNotNull(evictionType);
        }

        public EvictionType getEvictionType() {
            return evictionType;
        }
    }
}
