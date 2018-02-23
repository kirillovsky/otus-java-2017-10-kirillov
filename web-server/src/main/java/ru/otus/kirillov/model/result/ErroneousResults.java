package ru.otus.kirillov.model.result;

/**
 * Ответ об ошибке
 */
public final class ErroneousResults {

    /**
     * Причина. Возможно стоило добавить код
     */
    private final String cause;

    private ErroneousResults(String cause) {
        this.cause = cause;
    }

    public static ErroneousResults of(String cause) {
        return new ErroneousResults(cause);
    }
}
