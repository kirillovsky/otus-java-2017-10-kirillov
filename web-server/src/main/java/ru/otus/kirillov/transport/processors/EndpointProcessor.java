package ru.otus.kirillov.transport.processors;

import ru.otus.kirillov.utils.CommonUtils;
import java.util.function.Consumer;
import static ru.otus.kirillov.transport.Operations.Operation;
import static ru.otus.kirillov.transport.Results.*;

public abstract class EndpointProcessor {

    protected final Consumer<Operation> callback;

    public EndpointProcessor(Consumer<Operation> callback) {
        this.callback = CommonUtils.retunIfNotNull(callback);
    }

    public abstract void start();

    public abstract void send(Result result);
}
