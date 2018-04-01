package ru.otus.kirillov.model.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.common.NotAuthModelResult;
import ru.otus.kirillov.model.commands.common.SessionModelRequestWrapper;
import ru.otus.kirillov.model.services.auth.AuthResult;
import ru.otus.kirillov.model.services.auth.AuthService;
import ru.otus.kirillov.model.services.auth.AuthStatus;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;


public final class CommandInvoker {

    public static class UnknownOperationException extends RuntimeException {

        UnknownOperationException(ModelRequest rq) {
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

    public ModelResult execute(ModelRequest rq) {
        CommonUtils.requiredNotNull(rq);
        log.info("Start to process request - {}", rq);
        AuthResult authResult = authenticate(rq);

        if (authResult.getStatus() != AuthStatus.OK) {
            return new NotAuthModelResult(authResult.getAdditionalInfo());
        }

        final ModelRequest actualRq = unpackSessionRequest(rq);

        return commands.stream()
                .filter(c -> c.isApplicable(actualRq))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unable to process request - {}", actualRq);
                    return new UnknownOperationException(actualRq);
                })
                .execute(actualRq);
    }

    private AuthResult authenticate(ModelRequest modelRequest) {
        if (!(modelRequest instanceof SessionModelRequestWrapper)) {
            log.debug("No needs session context for rq - {}", modelRequest);
            return AuthResult.of(AuthStatus.OK);
        }

        SessionModelRequestWrapper<?> sessionRq = (SessionModelRequestWrapper<?>) modelRequest;

        if (!authService.isValidSession(sessionRq.getSessionId(), sessionRq.getUserName())) {
            log.error("Unable to authorize rq - {}", modelRequest);
            return AuthResult.of(AuthStatus.NOT_VALID,
                    String.format("Authentication failed (SessionId = %s, UserName = %s)",
                            sessionRq.getSessionId(), sessionRq.getUserName()));
        }
        log.info("Authorization succeed for modelRequest - {}", modelRequest);
        return AuthResult.of(AuthStatus.OK);
    }

    private ModelRequest unpackSessionRequest(ModelRequest rq) {
        return rq instanceof SessionModelRequestWrapper ? ((SessionModelRequestWrapper) rq).getRequest() : rq;
    }
}
