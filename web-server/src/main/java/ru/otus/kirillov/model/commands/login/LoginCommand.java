package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.utils.CommonUtils;

public class LoginCommand implements Command {

    private final AuthService authService;

    private final AESSecurity security = new AESSecurity();

    public LoginCommand(AuthService authService) {
        this.authService = CommonUtils.retunIfNotNull(authService);
    }

    @Override
    public boolean isApplicable(Request rq) {
        return rq instanceof LoginRequest;
    }

    @Override
    public Result execute(Request rq) {
        LoginRequest loginRequest = (LoginRequest) rq;
        try {
            Pair<String, String> encryptedPair =
                    encryptLoginPassword(loginRequest.getUserName(), loginRequest.getPassword());

            String sessionId = authService.login(encryptedPair.getLeft(), encryptedPair.getRight());
            return LoginResult.of(sessionId, encryptedPair.getLeft());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pair<String, String> encryptLoginPassword(String login, String password) {
        return Pair.of(security.encrypt(login), security.encrypt(password));
    }
}
