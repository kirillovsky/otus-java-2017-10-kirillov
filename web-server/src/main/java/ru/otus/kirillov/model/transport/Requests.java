package ru.otus.kirillov.model.transport;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.service.DBService;
import java.util.function.Function;

public final class Operations {

    private Operations() {
    }

    /**
     * Абстракция для работы с удаленными (как в контексте многопоточности, так и многопроцессности) операциями
     * @param <T> - тип удаленного сервиса
     * @param <T> - тип результата
     */
    public static abstract class Operation<T, R> {

        private final Header header;

        private final Function<T, R> remoteOp;

        public Operation(Function<T, R> remoteOp) {
            this.remoteOp = remoteOp;
            header = new Header();
        }

        public Header getHeader() {
            return header;
        }

        public Function<T, R> getRemoteOp() {
            return remoteOp;
        }
    }


    public static final class CacheOperation<R> extends Operation<CacheEngine, R> {

        public CacheOperation(Function<CacheEngine, R> remoteOp) {
            super(remoteOp);
        }
    }

    public static final class DbOperation<R> extends Operation<DBService, R> {
        public DbOperation(Function<DBService, R> remoteOp) {
            super(remoteOp);
        }
    }
}
