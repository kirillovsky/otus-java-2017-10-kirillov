package ru.otus.kirillov.controller;

import org.jetbrains.annotations.NotNull;
import ru.otus.kirillov.view.TemplateEngine;
import ru.otus.kirillov.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends AbstractServlet {

    public MainServlet(@NotNull TemplateEngine templateEngine) {
        super(templateEngine);
        templateEngine.initView(View.MAIN);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doAnswer(resp, getPage(req));
    }

    private String getPage(HttpServletRequest rq) {
        Map<String, Object> paramMap = new HashMap<>();
        if (checkAuthentication(rq)) {
            paramMap.put("isAuth", true);
            paramMap.put("username", getUserName(rq));
        } else {
            paramMap.put("isAuth", false);
        }
        return templateEngine.getPage(View.MAIN, paramMap);
    }
}
