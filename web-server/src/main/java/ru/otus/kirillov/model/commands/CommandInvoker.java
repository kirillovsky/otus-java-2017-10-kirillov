package ru.otus.kirillov.model.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.common.NotAuthResult;
import ru.otus.kirillov.model.commands.common.SessionRequestWrapper;
import ru.otus.kirillov.model.service.auth.AuthResult;
import ru.otus.kirillov.model.service.auth.AuthStatus;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public final class CommandInvoker {

    public static class UnknownOperationException extends RuntimeException {

        public UnknownOperationException(Request rq) {
            super("Not found operation for request - " + rq.toString());
        }
    }

    private final static Logger log = LogManager.getLogger();

    private final AuthService authService;

    private final List<Command> commands;

    public CommandInvoker(AuthService authService,
                          List<Command> operationCommands) {
        CommonUtils.requiredNotNull(authService, operationCommands);
        this.authService = authService;
        this.commands = new ArrayList<>(operationCommands);
    }

    public Result execute(Request rq) {
        CommonUtils.requiredNotNull(rq);
        log.info("Start to process request - {}", rq);
        AuthResult authResult = authenticate(rq);

        if (authResult.getStatus() != AuthStatus.OK) {
            return new NotAuthResult(authResult.getAdditionalInfo());
        }

        final Request actualRq = unpackSessionRequest(rq);

        return commands.stream()
                .filter(c -> c.isApplicable(actualRq))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unable to process request - {}", actualRq);
                    return new UnknownOperationException(actualRq);
                })
                .execute(actualRq);
    }

    private AuthResult authenticate(Request request) {
        if (!(request instanceof SessionRequestWrapper)) {
            log.debug("No needs session context for rq - {}", request);
            return AuthResult.of(AuthStatus.OK);
        }

        SessionRequestWrapper<?> sessionRq = (SessionRequestWrapper<?>) request;

        if (!authService.isValidSession(sessionRq.getSessionId(), sessionRq.getUserName())) {
            log.error("Unable to authorize rq - {}", request);
            return AuthResult.of(AuthStatus.NOT_VALID,
                    String.format("Authentication failed (SessionId = %s, UserName = %s)",
                            sessionRq.getSessionId(), sessionRq.getUserName()));
        }
        log.info("Authorization succeed for request - {}", request);
        return AuthResult.of(AuthStatus.OK);
    }

    private Request unpackSessionRequest(Request rq) {
        return rq instanceof SessionRequestWrapper ? ((SessionRequestWrapper) rq).getRequest() : rq;
    }
}
