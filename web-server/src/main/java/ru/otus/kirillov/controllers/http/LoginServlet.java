package ru.otus.kirillov.controllers.http;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.login.LoginModelRequest;
import ru.otus.kirillov.model.commands.login.LoginModelResult;
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
        ModelResult modelResult = authenticate(
                req.getParameter(USERNAME_PARAM_NAME), req.getParameter(PASSWORD_PARAM_NAME)
        );

        if (modelResult instanceof LoginModelResult) {
            setSessionCookies(resp, (LoginModelResult) modelResult);
        } else {
            resp.sendRedirect("/error?error-msg=" + ((ErroneousModelResult) modelResult).getCause());
            return;
        }

        doAnswer(resp, getPage(req, modelResult));
    }

    private ModelResult authenticate(String userName, String password) {
        return invoker.execute(LoginModelRequest.of(userName, password));
    }

    private String getPage(HttpServletRequest req, ModelResult modelResult) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isAuth", true);
        paramMap.put("username", ((LoginModelResult) modelResult).getUserName());
        return templateEngine.getPage(View.MAIN, paramMap);
    }
}
