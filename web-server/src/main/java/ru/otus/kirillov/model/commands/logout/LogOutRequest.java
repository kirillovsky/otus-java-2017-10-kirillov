package ru.otus.kirillov.model.commands.logout;

import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.utils.CommonUtils;

public class LogOutRequest implements Request {

    private final String sessionId;

    private final String userName;

    public LogOutRequest(String sessionId, String userName) {
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
}
