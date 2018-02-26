package ru.otus.kirillov.model.service.auth;

import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Заглушка для сервиса авторизации и авторизации
 */
public class StubAuthServiceImpl implements AuthService {

    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException(String userName, String password) {
            super(String.format("Wrong login/password (%s/%s) combination", userName, password));
        }
    }

    private static final AESSecurity SECURITYTOOLS = new AESSecurity();

    private static final String USERNAME = SECURITYTOOLS.encrypt("admin");

    private static final String PASSWORD = SECURITYTOOLS.encrypt("1234567890");

    private String currentSessionId = null;

    @Override
    public boolean isValidSession(String sessionId, String userName) {
        CommonUtils.requiredNotNull(sessionId, userName);
        return isSessionForUserExists(sessionId, userName);
    }

    @Override
    public String login(String userName, String password) {
        if (!Objects.equals(USERNAME, userName) || !Objects.equals(PASSWORD, password)) {
            throw new NotAuthorizedException(userName, password);
        }
        return currentSessionId = UUID.randomUUID().toString();
    }

    @Override
    public void logout(String sessionId, String userName) {
        CommonUtils.requiredNotNull(sessionId, userName);
        if (isSessionForUserExists(sessionId, userName)) {
            currentSessionId = null;
        }
    }

    private boolean isSessionForUserExists(String sessionId, String userName) {
        return Objects.equals(currentSessionId, sessionId) && Objects.equals(USERNAME, userName);
    }
}
