package ru.otus.kirillov.model.transport;

import ru.otus.kirillov.model.Observable;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.model.channels.DuplexChannel;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Синхронный клиент для взаимодействия с абстрактными каналами.
 * Данная реализация не предполагает его использование несколькими инстансами.
 * Т.е нет обратной маршрутизации. Может показаться странным, что данный клиент
 * сразу и {@link Observable} и {@link Observer}. Но это сделано, последний интерфейс нужен
 * для самоподписки на канал, а первый - для рассылки уведомлений подписчикам
 * @param <RQ>
 * @param <RS>
 */
public class MessageClient<RQ extends Message, RS extends Message> implements Observer<RS>, Observable<RS> {

    private final List<Observer<RS>> observers;
    private final DuplexChannel<RQ, RS> channel;

    public MessageClient(DuplexChannel<RQ, RS> channel) {
        this.observers = new ArrayList<>();
        this.channel = CommonUtils.retunIfNotNull(channel);
        channel.subscribe(this);
    }

    public void send(RQ rq) {
        channel.send(CommonUtils.retunIfNotNull(rq));
    }

    @Override
    public void receive(RS response) {
        observers.forEach(observer -> observer.receive(response));
    }

    @Override
    public void subscribe(Observer<RS> observable) {
        observers.add(CommonUtils.retunIfNotNull(observable));
    }
}
