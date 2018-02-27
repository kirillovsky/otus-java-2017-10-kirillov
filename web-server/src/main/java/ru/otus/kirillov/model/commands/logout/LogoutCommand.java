package ru.otus.kirillov.model.commands.logout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.service.auth.AuthService;

public class LogoutCommand implements Command {

    private static final Logger log = LogManager.getLogger();

    private final AuthService authService;

    public LogoutCommand(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean isApplicable(Request rq) {
        return rq instanceof LogOutRequest;
    }

    @Override
    public Result execute(Request rq) {
        Result rs;
        log.info("Try to process request - {}", rq);
        LogOutRequest actRq = (LogOutRequest) rq;
        try {
            authService.logout(actRq.getSessionId(), actRq.getUserName());
            rs = new LogOutResult();
        } catch (Exception e) {
            log.catching(e);
            rs = ErroneousResult.of(e.getMessage());
        }
        log.info("Request processed. Result - {}", rs);
        return rs;
    }
}
