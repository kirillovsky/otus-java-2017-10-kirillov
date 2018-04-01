package ru.otus.kirillov.model.services.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Заглушка для сервиса авторизации и авторизации
 */
public class StubAuthServiceImpl implements AuthService {

    private static final Logger log = LogManager.getLogger();

    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException(String userName, String password) {
            super(String.format("Wrong login/password (%s/%s) combination", userName, password));
        }
    }

    private final AESSecurity security;

    private final String username;

    private final String password;

    private Set<String> activeSessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public StubAuthServiceImpl(AESSecurity security) {
        this.security = CommonUtils.retunIfNotNull(security);
        username = "admin";
        password = security.encrypt("1234567890");
    }


    @Override
    public boolean isValidSession(String sessionId, String userName) {
        CommonUtils.requiredNotNull(sessionId, userName);
        return isSessionForUserExists(sessionId, userName);
    }

    @Override
    public String login(String userName, String password) {
        if (!Objects.equals(username, userName) || !Objects.equals(this.password, password)) {
            log.error("Username = {}, password = {} not authorized", userName, password);
            throw new NotAuthorizedException(userName, password);
        }
        String sessionId = UUID.randomUUID().toString();
        activeSessions.add(sessionId);
        log.info("Username = {}, password = {} authorization success. Session = {}", userName, password, sessionId);
        return sessionId;
    }

    @Override
    public void logout(String sessionId, String userName) {
        CommonUtils.requiredNotNull(sessionId, userName);
        if (isSessionForUserExists(sessionId, userName)) {
            activeSessions.remove(sessionId);
            log.info("Username = {}, Session = {}. Logout", userName, sessionId);
        }
    }

    private boolean isSessionForUserExists(String sessionId, String userName) {
        return activeSessions.contains(sessionId) && Objects.equals(username, userName);
    }
}
