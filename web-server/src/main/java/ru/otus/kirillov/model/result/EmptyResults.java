package ru.otus.kirillov.model.result;

/**
 * Набор пустых результатов
 */
public final class EmptyResults {

    private EmptyResults(){}

    /**
     * Валидация прошла успешно
     */
    public static class ValidationOK implements Result{}

    /**
     * Валидация не пройдена
     */
    public static class ValidationNotOK implements Result{
        private final String cause;

        public ValidationNotOK(String cause) {
            this.cause = cause;
        }
    }
}
