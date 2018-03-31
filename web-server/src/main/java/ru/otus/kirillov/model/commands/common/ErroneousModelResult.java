package ru.otus.kirillov.model.commands.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.Result;

/**
 * Ответ об ошибке
 */
public class ErroneousResult implements Result {

    /**
     * Причина. Возможно стоило добавить код
     */
    private final String cause;

    protected ErroneousResult(String cause) {
        this.cause = cause;
    }

    public static ErroneousResult of(String cause) {
        return new ErroneousResult(cause);
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cause", cause)
                .toString();
    }
}
