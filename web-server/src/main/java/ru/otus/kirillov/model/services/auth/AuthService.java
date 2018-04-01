package ru.otus.kirillov.model.services.auth;

public interface AuthService {

    boolean isValidSession(String sessionId, String userName);

    String login(String userName, String password);

    void logout(String sessionId, String userName);
}
