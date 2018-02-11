package ru.otus.kirillov.cacheengine.config;

import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import static ru.otus.kirillov.cacheengine.config.CacheEngineConfigKeys.*;

/**
 * Конфигурация кеша.
 * Created by Александр on 09.02.2018.
 */
public class CacheEngineConfig {

    private Map<CacheEngineConfigKey, Object> paramMap = new HashMap<>();

    public <T> T get(CacheEngineConfigKey<T> key) {
        return (T) paramMap.get(CommonUtils.returnIfNotNull(key));
    }

    public <T> T getOrDefault(CacheEngineConfigKey<T> key) {
        T result = get(key);
        return result != null ? result: key.getDefaultValue();
    }

    public <T> void put(CacheEngineConfigKey<T> key, T value) {
        paramMap.put(CommonUtils.returnIfNotNull(key), value);
    }

    public <T> CacheEngineConfig with(CacheEngineConfigKey<T> key, T value) {
        put(key, value);
        return this;
    }


}
