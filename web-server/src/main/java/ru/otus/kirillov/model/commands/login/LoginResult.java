package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.Result;

public class LoginResult implements Result {

    private final String sessionId;

    private final String userName;

    private LoginResult(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
    }

    public static LoginResult of(String sessionId, String userName) {
        return new LoginResult(sessionId, userName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sessionId", sessionId)
                .append("userName", userName)
                .toString();
    }
}
