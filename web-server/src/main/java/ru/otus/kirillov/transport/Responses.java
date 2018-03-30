package ru.otus.kirillov.transport;

public class Results {
    private Results() {}

    public interface Result {}

    public static class ValueResult<T> implements Result {
        private final T value;


        public ValueResult(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    public static class ExceptionResult<T extends Throwable> implements Result {
        private final T exception;

        public ExceptionResult(T exception) {
            this.exception = exception;
        }

        public T getException() {
            return exception;
        }

        public void rethrow() throws T {
            throw exception;
        }
    }
}
