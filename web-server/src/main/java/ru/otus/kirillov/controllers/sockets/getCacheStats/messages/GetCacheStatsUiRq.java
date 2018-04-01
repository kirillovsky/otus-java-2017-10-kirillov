package ru.otus.kirillov.controllers.sockets.getCacheStats.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.utils.CommonUtils;

public class GetCacheStatsUiRq {
    private String userName;
    private String sessionId;

    public GetCacheStatsUiRq(String userName, String sessionId) {
        this.userName = CommonUtils.retunIfNotNull(userName);
        this.sessionId = CommonUtils.retunIfNotNull(sessionId);
    }

    public GetCacheStatsUiRq() {}

    public String getUserName() {
        return userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userName", userName)
                .append("sessionId", sessionId)
                .toString();
    }
}
