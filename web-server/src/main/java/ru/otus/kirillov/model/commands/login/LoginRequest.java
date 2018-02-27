package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.Request;

/**
 * Запрос на вход в систему. Содержит в себе пару логин-пароль.
 * В каком бы виде они не были заданы
 */
public final class LoginRequest implements Request {

    private final String userName;

    private final String password;

    private LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public static LoginRequest of(String userName, String password) {
        return new LoginRequest(userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userName", userName)
                .append("password", password)
                .toString();
    }
}
