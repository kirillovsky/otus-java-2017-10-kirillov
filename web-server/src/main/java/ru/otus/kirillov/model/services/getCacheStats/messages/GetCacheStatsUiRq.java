package ru.otus.kirillov.model.service.getCacheStats.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetCacheStatsUiRq {
    private String userName;
    private String sessionId;

    public GetCacheStatsUiRq(String userName, String sessionId) {
        this.userName = userName;
        this.sessionId = sessionId;
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
