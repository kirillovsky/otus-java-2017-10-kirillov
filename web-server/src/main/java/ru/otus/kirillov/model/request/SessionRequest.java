package ru.otus.kirillov.model.request;

import ru.otus.kirillov.utils.CommonUtils;

/**
 * Запрос, для которого нужна сессия
 */
public final class SessionRequest<T extends Request> implements Request{

    /**
     * ID-сессии
     */
    private final String sessionId;

    /**
     * Имя пользователя (в обычном или зашифрованном виде)
     */
    private final String userName;

    /**
     * Результат
     */
    private final T request;

    private SessionRequest(String sessionId, String usetName, T request) {
        this.sessionId = sessionId;
        this.userName = usetName;
        this.request = request;
    }

    public static <T extends Request> SessionRequest of(String sessionId, String userName, T request) {
        CommonUtils.requiredNotNull(sessionId, request);
        return new SessionRequest(sessionId, userName, request);
    }

    public String getSessionId() {
        return sessionId;
    }

    public T getRequest() {
        return request;
    }
}
