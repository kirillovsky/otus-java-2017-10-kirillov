package ru.otus.kirillov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.login.LoginRequest;
import ru.otus.kirillov.model.commands.login.LoginResult;
import ru.otus.kirillov.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends AbstractServlet {

    private static final String USERNAME_PARAM_NAME = "username";
    private static final String PASSWORD_PARAM_NAME = "password";

    @Autowired
    protected CommandInvoker invoker;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Result result = authenticate(
                req.getParameter(USERNAME_PARAM_NAME), req.getParameter(PASSWORD_PARAM_NAME)
        );

        if (result instanceof LoginResult) {
            setSessionCookies(resp, (LoginResult) result);
        } else {
            resp.sendRedirect("/error?error-msg=" + ((ErroneousResult) result).getCause());
            return;
        }

        doAnswer(resp, getPage(req, result));
    }

    private Result authenticate(String userName, String password) {
        return invoker.execute(LoginRequest.of(userName, password));
    }

    private String getPage(HttpServletRequest req, Result result) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isAuth", true);
        paramMap.put("username", ((LoginResult) result).getUserName());
        return templateEngine.getPage(View.MAIN, paramMap);
    }
}
