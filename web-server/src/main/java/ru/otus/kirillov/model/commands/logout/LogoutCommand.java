package ru.otus.kirillov.model.commands.logout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.services.auth.AuthService;

public class LogoutCommand implements Command {

    private static final Logger log = LogManager.getLogger();

    private final AuthService authService;

    public LogoutCommand(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean isApplicable(ModelRequest rq) {
        return rq instanceof LogOutModelRequest;
    }

    @Override
    public ModelResult execute(ModelRequest rq) {
        ModelResult rs;
        log.info("Try to process request - {}", rq);
        LogOutModelRequest actRq = (LogOutModelRequest) rq;
        try {
            authService.logout(actRq.getSessionId(), actRq.getUserName());
            rs = new LogOutModelResult();
        } catch (Exception e) {
            log.catching(e);
            rs = ErroneousModelResult.of(e.getMessage());
        }
        log.info("Response processed. Response - {}", rs);
        return rs;
    }
}
