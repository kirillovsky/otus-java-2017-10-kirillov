package ru.otus.kirillov.controllers.http;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.common.SessionModelRequestWrapper;
import ru.otus.kirillov.model.commands.logout.LogOutModelRequest;
import ru.otus.kirillov.model.commands.logout.LogOutModelResult;
import ru.otus.kirillov.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.singletonMap;

public class LogoutServlet extends AbstractServlet {

    @Autowired
    protected CommandInvoker invoker;

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
        ModelResult rs = doLogout(createRequest(req));
        deleteSessionCookies(resp, req);
        if (!(rs instanceof LogOutModelResult)) {
            resp.sendRedirect("/error?error-msg=" + ((ErroneousModelResult) rs).getCause());
            return;
        }
        doAnswer(resp, getPage(req, rs));
    }

    private ModelRequest createRequest(HttpServletRequest request) {
        String username = getUserName(request);
        String sessionId = getSessionId(request);
        return SessionModelRequestWrapper.of(sessionId, username,
                new LogOutModelRequest(sessionId, username)
        );
    }

    private ModelResult doLogout(ModelRequest rq) {
        return invoker.execute(rq);
    }

    private String getPage(HttpServletRequest rq, ModelResult rs) {
        return templateEngine.getPage(View.MAIN, singletonMap("isAuth", false));
    }
}
