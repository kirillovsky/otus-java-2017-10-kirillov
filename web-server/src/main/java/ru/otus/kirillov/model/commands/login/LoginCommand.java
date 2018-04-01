package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.services.auth.AuthService;
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
    public boolean isApplicable(ModelRequest rq) {
        return rq instanceof LoginModelRequest;
    }

    @Override
    public ModelResult execute(ModelRequest rq) {
        ModelResult rs;
        log.info("Try to process request {}", rq);
        try {
            LoginModelRequest loginRequest = (LoginModelRequest) rq;
            Pair<String, String> encryptedPair =
                    encryptLoginPassword(loginRequest.getUserName(), loginRequest.getPassword());
            log.debug("Encrypt session data (userName={}, password={})",
                    encryptedPair.getLeft(), encryptedPair.getRight());

            String sessionId = authService.login(encryptedPair.getLeft(), encryptedPair.getRight());
            rs = LoginModelResult.of(sessionId, encryptedPair.getLeft());
        } catch (Exception e) {
            log.catching(e);
            rs = ErroneousModelResult.of(e.getMessage());
        }
        log.info("Response processed. Response - {}", rs);
        return rs;
    }

    private Pair<String, String> encryptLoginPassword(String login, String password) {
        return Pair.of(login, security.encrypt(password));
    }
}
