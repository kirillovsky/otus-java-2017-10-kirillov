package ru.otus.kirillov.model.transport;

public final class Responses {

    private Responses() {}

    public static abstract class Response extends Message {
        public Response(Header header) {
            super(header);
        }
    }

    public static class ValueResponse<T> extends Response {
        private final T value;


        public ValueResponse(T value, Header header) {
            super(header);
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    public static class ExceptionResponse<T extends Throwable> extends Response {
        private final T exception;

        public ExceptionResponse(T exception, Header header) {
            super(header);
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
