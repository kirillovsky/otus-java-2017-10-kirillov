package ru.otus.kirillov.model.commands.getCacheStats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.ModelResult;
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
    public boolean isApplicable(ModelRequest rq) {
        return rq instanceof GetCacheStatsModelRequest;
    }

    @Override
    public ModelResult execute(ModelRequest rq) {
        log.info("Try to process rq {}", rq);
        ModelResult modelResult;
        try {
            modelResult = GetCacheStatsModelResult.of(cacheEngine.getStats());
        } catch (Exception e) {
            log.catching(e);
            modelResult = ErroneousModelResult.of(e.getMessage());
        }
        log.info("Response processed. Response {}", modelResult);
        return modelResult;
    }
}
