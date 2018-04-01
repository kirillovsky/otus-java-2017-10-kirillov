package ru.otus.kirillov.controllers.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.kirillov.model.commands.login.LoginModelResult;
import ru.otus.kirillov.view.TemplateEngine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class AbstractServlet extends HttpServlet {

    protected static final String COOKIE_USERNAME_PARAM_NAME = "USERNAME";
    protected static final String COOKIE_SESSION_ID_PARAM_NAME = "SESSION_ID";
    protected static final int COOKIE_MAX_AGE_SEC = 1 * 60 * 60;

    @Autowired
    protected TemplateEngine templateEngine;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void setSessionCookies(HttpServletResponse resp, LoginModelResult result) {
        resp.addCookie(createSessionCookie(COOKIE_USERNAME_PARAM_NAME, result.getUserName()));
        resp.addCookie(createSessionCookie(COOKIE_SESSION_ID_PARAM_NAME, result.getSessionId()));
    }

    protected void deleteSessionCookies(HttpServletResponse rs, HttpServletRequest rq) {
        dropSessionCookie(rs, rq, COOKIE_USERNAME_PARAM_NAME);
        dropSessionCookie(rs, rq, COOKIE_SESSION_ID_PARAM_NAME);
    }

    protected void dropSessionCookie(HttpServletResponse rs, HttpServletRequest rq, String cookieName) {
        Cookie cookie = getSessionCookie(rq, cookieName);
        cookie.setMaxAge(0);
        rs.addCookie(cookie);
    }

    protected Cookie createSessionCookie(String name, String val) {
        Cookie result = new Cookie(name, val);
        result.setMaxAge(COOKIE_MAX_AGE_SEC);
        return result;
    }

    protected boolean hasCookieWithName(HttpServletRequest req, String name) {
        return Arrays.stream(req.getCookies())
                .anyMatch(cookie -> cookie.getName().equals(name));
    }

    private Cookie getSessionCookie(HttpServletRequest req, String key) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found cookie for key - " + key));
    }

    protected String getSessionCookieValue(HttpServletRequest req, String key) {
        return getSessionCookie(req, key).getValue();
    }

    protected void doAnswer(HttpServletResponse response, String htmlString) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(htmlString);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    protected String getUserName(HttpServletRequest request) {
        return getSessionCookieValue(request, COOKIE_USERNAME_PARAM_NAME);
    }

    protected boolean hasUserName(HttpServletRequest request) {
        return hasCookieWithName(request, COOKIE_USERNAME_PARAM_NAME);
    }

    protected String getSessionId(HttpServletRequest request) {
        return getSessionCookieValue(request, COOKIE_SESSION_ID_PARAM_NAME);
    }

    protected boolean hasSessionId(HttpServletRequest rq) {
        return hasCookieWithName(rq, COOKIE_SESSION_ID_PARAM_NAME);
    }

    protected boolean checkAuthentication(HttpServletRequest rq) {
        return hasUserName(rq) && hasSessionId(rq);
    }
}
