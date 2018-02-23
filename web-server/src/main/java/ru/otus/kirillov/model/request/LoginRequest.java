package ru.otus.kirillov.model.request;

/**
 * Запрос на вход в систему. Содержит в себе пару логин-пароль.
 * В каком бы виде они не были заданы
 */
public final class LoginRequest {

    private final String userName;

    private final String password;

    private LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public static LoginRequest of(String userName, String password) {
        return new LoginRequest(userName, password);
    }
}
