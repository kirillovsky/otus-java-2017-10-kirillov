package ru.otus.kirillov.model.transport;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.service.DBService;
import java.util.function.Function;

public final class Requests {

    private Requests() {
    }

    public static abstract class Request extends Message {
        protected Request() {
        }
    }

    /**
     * Абстракция для работы с удаленными (как в контексте многопоточности, так и многопроцессности) операциями
     *
     * @param <T> - тип удаленного сервиса
     * @param <R> - тип результата
     */
    public static abstract class RemoteOperation<T, R> extends Request {

        private final Function<T, R> remoteOp;

        public RemoteOperation(Function<T, R> remoteOp) {
            this.remoteOp = remoteOp;
        }

        public Function<T, R> getRemoteOp() {
            return remoteOp;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("remoteOp", remoteOp)
                    .toString();
        }
    }

    public static class RemoteCacheOperation<R> extends RemoteOperation<CacheEngine, R> {

        public RemoteCacheOperation(Function<CacheEngine, R> remoteOp) {
            super(remoteOp);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append(super.toString())
                    .toString();
        }
    }

    public static class RemoteDbOperation<R> extends RemoteOperation<DBService, R> {
        public RemoteDbOperation(Function<DBService, R> remoteOp) {
            super(remoteOp);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append(super.toString())
                    .toString();
        }
    }
}
