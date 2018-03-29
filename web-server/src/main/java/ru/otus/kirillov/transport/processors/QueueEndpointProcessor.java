package ru.otus.kirillov.transport.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import static ru.otus.kirillov.transport.Operations.Operation;
import static ru.otus.kirillov.transport.Results.Result;

public class QueueEndpointProcessor extends EndpointProcessor {

    private static final Logger log = LogManager.getLogger();

    private final BlockingQueue<Operation> inQueue;
    private final BlockingQueue<Result> outQueue;

    public QueueEndpointProcessor(BlockingQueue<Operation> inQueue, BlockingQueue<Result> outQueue, Consumer<Operation> callback) {
        super(callback);
        this.inQueue = CommonUtils.retunIfNotNull(inQueue);
        this.outQueue = CommonUtils.retunIfNotNull(outQueue);
    }

    @Override
    public void start() {
        try {
            callback.accept(inQueue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(Result result) {
        CommonUtils.requiredNotNull(result);
        outQueue.offer(result);
    }
}
