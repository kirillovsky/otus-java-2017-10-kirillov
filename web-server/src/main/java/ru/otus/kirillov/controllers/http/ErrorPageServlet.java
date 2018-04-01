package ru.otus.kirillov.controllers.http;

import ru.otus.kirillov.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ErrorPageServlet extends AbstractServlet {

    private static final String ERROR_MSG_PARAM_NAME = "error-msg";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorCause = req.getParameter(ERROR_MSG_PARAM_NAME);
        doAnswer(resp, getErrorPage(req, errorCause));
    }

    protected String getErrorPage(HttpServletRequest request, String cause) {
        Map<String, Object> paramMap = new HashMap<>();
        boolean isAuth = checkAuthentication(request);
        paramMap.put("isAuth", isAuth);
        if (isAuth) {
            paramMap.put("username", getSessionCookieValue(request, COOKIE_USERNAME_PARAM_NAME));
        }
        paramMap.put("error-msg", cause);
        return templateEngine.getPage(View.ERROR, paramMap);
    }
}
