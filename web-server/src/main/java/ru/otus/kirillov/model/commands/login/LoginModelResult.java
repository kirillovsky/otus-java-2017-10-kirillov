package ru.otus.kirillov.model.commands.login;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.ModelResult;

public class LoginModelResult implements ModelResult {

    private final String sessionId;

    private final String userName;

    private LoginModelResult(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
    }

    public static LoginModelResult of(String sessionId, String userName) {
        return new LoginModelResult(sessionId, userName);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sessionId", sessionId)
                .append("userName", userName)
                .toString();
    }
}
