package ru.otus.kirillov.controllers.sockets.getCacheStats.messages;

public class GetCacheStatsUiErrorRs {
    private final String cause;

    public GetCacheStatsUiErrorRs(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}
