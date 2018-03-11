package ru.otus.kirillov.controller;

import org.jetbrains.annotations.NotNull;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.common.SessionRequestWrapper;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsRequest;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsResult;
import ru.otus.kirillov.view.TemplateEngine;
import ru.otus.kirillov.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetCacheStatsServlet extends AbstractServlet {

    protected final CommandInvoker invoker;

    public GetCacheStatsServlet(@NotNull TemplateEngine templateEngine, @NotNull CommandInvoker invoker) {
        super(templateEngine);
        this.invoker = invoker;
        templateEngine.initView(View.CACHE_STATS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        return templateEngine.getPage(View.CACHE_STATS, paramMap);
    }
}
