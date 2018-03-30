package ru.otus.kirillov.model.transport;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.service.DBService;
import java.util.function.Function;

public final class Requests {

    private Requests() {
    }

    public static abstract class Request {
        private final Header header;

        protected Request() {
            header = new Header();
        }

        public Header getHeader() {
            return header;
        }
    }

    /**
     * Абстракция для работы с удаленными (как в контексте многопоточности, так и многопроцессности) операциями
     * @param <T> - тип удаленного сервиса
     * @param <T> - тип результата
     */
    public static abstract class RemoteOperation<T, R> extends Request{

        private final Function<T, R> remoteOp;

        public RemoteOperation(Function<T, R> remoteOp) {
            this.remoteOp = remoteOp;
        }

        public Function<T, R> getRemoteOp() {
            return remoteOp;
        }
    }

    public static final class RemoteCacheOperation<R> extends RemoteOperation<CacheEngine, R> {

        public RemoteCacheOperation(Function<CacheEngine, R> remoteOp) {
            super(remoteOp);
        }
    }

    public static final class RemoteDbOperation<R> extends RemoteOperation<DBService, R> {
        public RemoteDbOperation(Function<DBService, R> remoteOp) {
            super(remoteOp);
        }
    }
}
