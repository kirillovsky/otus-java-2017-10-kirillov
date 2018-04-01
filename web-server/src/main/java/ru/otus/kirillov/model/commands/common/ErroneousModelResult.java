package ru.otus.kirillov.model.commands.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.ModelResult;

/**
 * Ответ об ошибке
 */
public class ErroneousModelResult implements ModelResult {

    /**
     * Причина. Возможно стоило добавить код
     */
    private final String cause;

    protected ErroneousModelResult(String cause) {
        this.cause = cause;
    }

    public static ErroneousModelResult of(String cause) {
        return new ErroneousModelResult(cause);
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
