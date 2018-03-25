package ru.otus.kirillov.model.service.getCacheStats.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetCacheStatsRq {
    private String clientKey;
    private String userName;
    private String sessionId;

    public GetCacheStatsRq(String clientKey, String userName, String sessionId) {
        this.clientKey = clientKey;
        this.userName = userName;
        this.sessionId = sessionId;
    }

    public GetCacheStatsRq() {}

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientKey", clientKey)
                .append("userName", userName)
                .append("sessionId", sessionId)
                .toString();
    }
}
