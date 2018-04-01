package ru.otus.kirillov.model.commands.remote;

import ru.otus.kirillov.model.commands.ModelResult;
import java.util.concurrent.CompletableFuture;

public class RemoteOpsModelResult<R> implements ModelResult {

    private final CompletableFuture<R> asyncResult;

    public RemoteOpsModelResult(CompletableFuture<R> asyncResult) {
        this.asyncResult = asyncResult;
    }

    public CompletableFuture<R> getAsyncResult() {
        return asyncResult;
    }
}
