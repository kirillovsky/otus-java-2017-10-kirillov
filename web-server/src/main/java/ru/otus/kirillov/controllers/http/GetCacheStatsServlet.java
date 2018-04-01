package ru.otus.kirillov.controllers.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.AuthenticationRequest;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.view.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!checkAuthentication(req)) {
            resp.sendRedirect("/main");
            return;
        }
        ModelResult commandModelResult = auth(createRequest(req));
        if (commandModelResult instanceof ErroneousModelResult) {
            resp.sendRedirect("/error?error-msg="+((ErroneousModelResult) commandModelResult).getCause());
            return;
        }

        String page = getPage(req);
        doAnswer(resp, page);
    }

    private ModelRequest createRequest(HttpServletRequest rq) {
        return AuthenticationRequest.of(getSessionId(rq), getUserName(rq));
    }

    private ModelResult auth(ModelRequest rq) {
        return invoker.execute(rq);
    }

    private String getPage(HttpServletRequest rq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isAuth", true);
        paramMap.put("username", getUserName(rq));
        paramMap.put("cacheStatsPort", cacheStatPortNumber);
        paramMap.put("cacheStatsEndpoint", endpoint);
        paramMap.put("host", host);
        paramMap.put("port", portNumber);
        paramMap.put("cacheStatsDelay", cacheInfoRefreshDelayInMsl);
        paramMap.put("sessionIdKey", COOKIE_SESSION_ID_PARAM_NAME);
        paramMap.put("userNameKey", COOKIE_USERNAME_PARAM_NAME);
        return templateEngine.getPage(View.CACHE_STATS, paramMap);
    }
}
