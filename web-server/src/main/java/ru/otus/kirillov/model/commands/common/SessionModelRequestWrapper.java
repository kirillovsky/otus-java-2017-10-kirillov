package ru.otus.kirillov.model.commands.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Запрос, для которого нужна сессия
 */
public class SessionModelRequestWrapper<T extends ModelRequest> implements ModelRequest {

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

    protected SessionModelRequestWrapper(String sessionId, String userName, T request) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.request = request;
    }

    public static <T extends ModelRequest> SessionModelRequestWrapper<T>
    of(String sessionId, String userName, T request) {
        CommonUtils.requiredNotNull(sessionId, request);
        return new SessionModelRequestWrapper(sessionId, userName, request);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public T getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sessionId", sessionId)
                .append("userName", userName)
                .append("request", request)
                .toString();
    }
}
