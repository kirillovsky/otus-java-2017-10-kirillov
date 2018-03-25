package ru.otus.kirillov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.common.SessionRequestWrapper;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsRequest;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsResult;
import ru.otus.kirillov.view.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetCacheStatsServlet extends AbstractServlet {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private CommandInvoker invoker;

    @Value("${frontend.cache-stats.port}")
    private String cacheStatPortNumber;

    @Value("${frontend.cache-stats.endpoint}")
    private String endpoint;

    @Value("${frontend.host}")
    private String host;

    @Value("${frontend.port}")
    private String portNumber;

    @Value("${frontend.cache-stats.refresh.delay}")
    private String cacheInfoRefreshDelayInMsl;

    private String clientKey = UUID.randomUUID().toString();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!checkAuthentication(req)) {
            resp.sendRedirect("/main");
            return;
        }
        Result commandResult = getCacheStats(createRequest(req));
        if (!(commandResult instanceof GetCacheStatsResult)) {
            resp.sendRedirect("/error?error-msg="+((ErroneousResult)commandResult).getCause());
            return;
        }

        String page = getPage(req, commandResult);
        doAnswer(resp, page);
    }

    private Request createRequest(HttpServletRequest rq) {
        return SessionRequestWrapper.of(
                getSessionId(rq), getUserName(rq), new GetCacheStatsRequest()
        );
    }

    private Result getCacheStats(Request rq) {
        return invoker.execute(rq);
    }

    private String getPage(HttpServletRequest rq, Result result) {
        GetCacheStatsResult rs = (GetCacheStatsResult) result;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isAuth", true);
        paramMap.put("username", getUserName(rq));
        paramMap.put("cacheSize", rs.getCacheSize());
        paramMap.put("cacheHit", rs.getCacheHit());
        paramMap.put("cacheMiss", rs.getCacheMiss());
        paramMap.put("frontend.cache-stats.port", cacheStatPortNumber);
        paramMap.put("frontend.cache-stats.endpoint", endpoint);
        paramMap.put("frontend.host", host);
        paramMap.put("frontend.port", portNumber);
        paramMap.put("frontend.cache-stats.refresh.delay", cacheInfoRefreshDelayInMsl);
        paramMap.put("client-key", clientKey);
        paramMap.put("sessionIdKey", COOKIE_SESSION_ID_PARAM_NAME);
        paramMap.put("userNameKey", COOKIE_USERNAME_PARAM_NAME);
        return templateEngine.getPage(View.CACHE_STATS, paramMap);
    }
}
