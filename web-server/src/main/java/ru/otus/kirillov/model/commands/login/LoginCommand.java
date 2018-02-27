package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.utils.CommonUtils;

public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger();

    private final AuthService authService;

    private final AESSecurity security;

    public LoginCommand(AuthService authService, AESSecurity security) {
        this.authService = CommonUtils.retunIfNotNull(authService);
        this.security = CommonUtils.retunIfNotNull(security);
    }

    @Override
    public boolean isApplicable(Request rq) {
        return rq instanceof LoginRequest;
    }

    @Override
    public Result execute(Request rq) {
        Result rs;
        log.info("Try to process request {}", rq);
        try {
            LoginRequest loginRequest = (LoginRequest) rq;
            Pair<String, String> encryptedPair =
                    encryptLoginPassword(loginRequest.getUserName(), loginRequest.getPassword());
            log.debug("Encrypt session data (userName={}, password={})",
                    encryptedPair.getLeft(), encryptedPair.getRight());

            String sessionId = authService.login(encryptedPair.getLeft(), encryptedPair.getRight());
            rs = LoginResult.of(sessionId, encryptedPair.getLeft());
        } catch (Exception e) {
            log.catching(e);
            rs = ErroneousResult.of(e.getMessage());
        }
        log.info("Request processed. Result - {}", rs);
        return rs;
    }

    private Pair<String, String> encryptLoginPassword(String login, String password) {
        return Pair.of(security.encrypt(login), security.encrypt(password));
    }
}
