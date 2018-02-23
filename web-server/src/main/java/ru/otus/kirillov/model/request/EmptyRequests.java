package ru.otus.kirillov.model.request;

/**
 * Набор пустых реквестов
 */
public final class EmptyRequests {

    private EmptyRequests() {}

    /**
     * Запрос на получение сстояния кэша
     */
    public static class CacheStatsRequest implements Request {
    }

    public static class LogOutRequest implements Request {}
}
