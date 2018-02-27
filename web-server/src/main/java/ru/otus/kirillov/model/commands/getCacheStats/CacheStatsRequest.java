package ru.otus.kirillov.model.commands.getCacheStats;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.Request;

/**
 * Запрос на получение сстояния кэша
 */
public class CacheStatsRequest implements Request {

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
