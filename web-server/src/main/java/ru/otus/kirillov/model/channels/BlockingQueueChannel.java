package ru.otus.kirillov.model.channels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import static ru.otus.kirillov.utils.WorkerUtils.initWorkers;

/**
 * Однонаправленный канал, построенный на блокирующей очереди.
 * "Отъедает" несколько потоков, для нотификации подписчков.
 * ВНИМАНИЕ! Предполагается, что код обработчика входящего сообшения не будет
 * заьтратным по времени, так как это может сказаться на производительности потоков
 *
 * @param <T> - тип сообщения
 */
public class BlockingQueueChannel<T> implements Channel<T> {

    private static final Logger log = LogManager.getLogger();

    private final BlockingQueue<T> inQueue;
    private final List<Observer<T>> observers;

    public BlockingQueueChannel(ExecutorService service, int workersCount) {
        this(new LinkedBlockingQueue<>(), service, workersCount);
    }

    public BlockingQueueChannel(BlockingQueue<T> inQueue, ExecutorService service, int workersCount) {
        this.inQueue = CommonUtils.retunIfNotNull(inQueue);
        this.observers = new ArrayList<>();
        initWorkers(service, workersCount, () -> {
            T msg = inQueue.take();
            log.info("Message {} received!", msg);
            observers.forEach(o -> o.notify(msg));
        });
    }

    @Override
    public void send(T msg) {
        log.debug("Send message - {}", msg);
        inQueue.offer(msg);
    }

    @Override
    public void subscribe(Observer<T> observable) {
        observers.add(observable);
    }
}
