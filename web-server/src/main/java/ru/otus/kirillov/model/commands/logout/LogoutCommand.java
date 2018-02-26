package ru.otus.kirillov.model.commands.logout;

import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.service.auth.AuthService;

public class LogoutCommand implements Command {

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
        LogOutRequest actRq = (LogOutRequest) rq;
        try {
            authService.logout(actRq.getSessionId(), actRq.getUserName());
            return new LogOutResult();
        } catch (Exception e) {
            return ErroneousResult.of(e.getMessage());
        }
    }
}
