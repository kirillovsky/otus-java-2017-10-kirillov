package ru.otus.kirillov.model.channels;

import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Простейший вариант обработчика дуплексного канала.
 * Для многопроцессной реализации может быть заменен на реализацию на netty-socketio
 */
public class SimpleDuplexChannel<RQ, RS> implements DuplexChannel<RQ, RS>, Observer<RS> {

    private final Channel<RQ> inChannel;
    private final List<Observer<RS>> observers;

    public SimpleDuplexChannel(Channel<RQ> inQueue, Channel<RS> outQueue) {
        observers = new ArrayList<>();
        inChannel = CommonUtils.retunIfNotNull(inQueue);
        Channel<RS> outChannel = CommonUtils.retunIfNotNull(outQueue);
        outChannel.subscribe(this);
    }

    @Override
    public void send(RQ response) {
        inChannel.send(response);
    }

    @Override
    public void subscribe(Observer<RS> observable) {
        observers.add(observable);
    }

    @Override
    public void receive(RS response) {
        observers.forEach(o -> o.receive(response));
    }
}
