package ru.otus.kirillov.transport;

import ru.otus.kirillov.utils.CommonUtils;

public class Responses {
    private Responses() {}

    public static abstract class Response {
        private final Header header;

        public Response(Header header) {
            this.header = CommonUtils.retunIfNotNull(header);
        }

        public Header getHeader() {
            return header;
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
