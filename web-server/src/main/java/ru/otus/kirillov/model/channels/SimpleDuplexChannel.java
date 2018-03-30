package ru.otus.kirillov.model.channels;

import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Простейший вариант обработчика дуплексного канала.
 * Для многопроцессной реализации может быть заменен на реализацию на netty-socketio
 */
public class SimpleDuplexChannel<RQ, RS> implements DuplexChannel<RQ, RS> {

    private final Channel<RQ> inChannel;
    private final Channel<RS> outChannel;

    public SimpleDuplexChannel(Channel<RQ> inQueue, Channel<RS> outQueue) {
        this.inChannel = CommonUtils.retunIfNotNull(inQueue);
        this.outChannel = CommonUtils.retunIfNotNull(outQueue);
    }

    @Override
    public void send(RQ response) {
        inChannel.send(response);
    }

    @Override
    public void subscribe(Observer<RS> observable) {
        outChannel.subscribe(observable);
    }
}
