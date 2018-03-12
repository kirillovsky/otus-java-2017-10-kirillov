package ru.otus.kirillov.controller;

import org.jetbrains.annotations.NotNull;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.common.SessionRequestWrapper;
import ru.otus.kirillov.model.commands.logout.LogOutRequest;
import ru.otus.kirillov.model.commands.logout.LogOutResult;
import ru.otus.kirillov.view.TemplateEngine;
import ru.otus.kirillov.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.singletonMap;

public class LogoutServlet extends AbstractServlet {

    protected final CommandInvoker invoker;

    public LogoutServlet(@NotNull TemplateEngine templateEngine, @NotNull CommandInvoker invoker) {
        super(templateEngine);
        this.invoker = invoker;
        templateEngine.initView(View.MAIN);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkAuthentication(req)) {
            resp.sendRedirect("/main");
            return;
        }
        Result rs = doLogout(createRequest(req));
        deleteSessionCookies(resp, req);
        if (!(rs instanceof LogOutResult)) {
            resp.sendRedirect("/error?error-msg=" + ((ErroneousResult) rs).getCause());
            return;
        }
        doAnswer(resp, getPage(req, rs));
    }

    private Request createRequest(HttpServletRequest request) {
        String username = getUserName(request);
        String sessionId = getSessionId(request);
        return SessionRequestWrapper.of(sessionId, username,
                new LogOutRequest(sessionId, username)
        );
    }

    private Result doLogout(Request rq) {
        return invoker.execute(rq);
    }

    private String getPage(HttpServletRequest rq, Result rs) {
        return templateEngine.getPage(View.MAIN, singletonMap("isAuth", false));
    }
}
