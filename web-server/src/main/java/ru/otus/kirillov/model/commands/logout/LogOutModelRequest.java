package ru.otus.kirillov.model.commands.logout;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.utils.CommonUtils;

public class LogOutModelRequest implements ModelRequest {

    private final String sessionId;

    private final String userName;

    public LogOutModelRequest(String sessionId, String userName) {
        CommonUtils.requiredNotNull(sessionId, userName);
        this.sessionId = sessionId;
        this.userName = userName;
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
