package ru.otus.kirillov.model.commands.getCacheStats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Команда получения статистики по кэшу
 */
public class GetCacheStatsCommand implements Command {

    private static final Logger log = LogManager.getLogger();

    private final CacheEngine cacheEngine;

    public GetCacheStatsCommand(CacheEngine cacheEngine) {
        this.cacheEngine = CommonUtils.retunIfNotNull(cacheEngine);
    }

    @Override
    public boolean isApplicable(Request rq) {
        return rq instanceof CacheStatsRequest;
    }

    @Override
    public Result execute(Request rq) {
        log.info("Try to process rq {}", rq);
        Result result;
        try {
            result = CacheStatsResult.of(cacheEngine.getStats());
        } catch (Exception e) {
            log.catching(e);
            result = ErroneousResult.of(e.getMessage());
        }
        log.info("Request processed. Result {}", result);
        return result;
    }
}