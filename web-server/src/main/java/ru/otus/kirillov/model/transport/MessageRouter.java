package ru.otus.kirillov.model.transport;

import ru.otus.kirillov.model.Observable;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.model.channels.BlockingQueueChannel;
import ru.otus.kirillov.model.channels.Channel;
import ru.otus.kirillov.model.channels.DuplexChannel;
import ru.otus.kirillov.model.transport.Requests.Request;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Маршрутизатор входящих запросов по типу сообщения.
 * Реализует интерфейс {@link Observable}, однако гарантирует, что
 * сообщение получит только тот подписчик, который направил соответствующий запрос.
 * Реализация роутера для многопоточной системы подразумевает, что каждый тип запроса можно отправить
 * только в определенный канал.
 * Опять же, данная реализация подразумевает, что {@link Header} уникален в рамках системы
 * хотя бы в промежутке обработки запроса
 */
public class MessageRouter implements Observer<Response> {

    private Channel<Response> backwardChannel;
    private final Map<Header, Observer<Response>> messageCorrelations;
    private Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap;

    public MessageRouter(Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap,
                         ExecutorService service, int workersCount) {
        messageCorrelations = new ConcurrentHashMap<>();
        initBackwardFlow(service, workersCount);
        initDirectFlow(outgoingChannelsMap);
    }

    private void initDirectFlow(Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap) {
        this.outgoingChannelsMap = new HashMap<>(CommonUtils.retunIfNotNull(outgoingChannelsMap));
        subscribeToAllOutgoingChannels();
    }

    private void initBackwardFlow(ExecutorService service, int workersCount) {
        backwardChannel = new BlockingQueueChannel<>(service, workersCount);
        backwardChannel.subscribe((rs) -> {
            Header rsHeader = rs.getHeader();
            //Ищем корреляцию ответа
            Observer<Response> sender = messageCorrelations.computeIfAbsent(rsHeader, (h) -> {
                throw new RuntimeException(
                        String.format("Not found correlation for response header - %s. Response will reject.", rsHeader)
                );
            });
            //Удаляем ее
            messageCorrelations.remove(rsHeader);
            //Отправка получателю
            sender.notify(rs);
        });
    }

    private void subscribeToAllOutgoingChannels() {
        outgoingChannelsMap.forEach((clazz, channel) -> channel.subscribe(this));
    }

    public void send(Observer<Response> sender, Request rq) {
        //1. Ищем канал если не нашли, кидаем экспешн
        DuplexChannel<Request, Response> channel = outgoingChannelsMap.computeIfAbsent(rq.getClass(), (c) -> {
            throw new RuntimeException("Not found channel for incoming request type - " + rq.getClass());
        });
        //2. Сохраняем корреляцию запроса
        messageCorrelations.put(rq.getHeader(), sender);
        //3. А теперь можно отправлять
        channel.send(rq);
    }

    @Override
    public void notify(Response response) {
        backwardChannel.send(response);
    }
}
