package ru.otus.kirillov.model.transport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.Observable;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.model.channels.Channel;
import ru.otus.kirillov.model.channels.DuplexChannel;
import ru.otus.kirillov.model.transport.Requests.Request;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Маршрутизатор входящих запросов по типу сообщения.
 * Гарантирует, что сообщение получит только тот подписчик, который направил соответствующий запрос.
 * Реализация роутера для многопоточной системы подразумевает, что каждый тип запроса можно отправить
 * только в определенный выходной канал.
 * Опять же, данная реализация подразумевает, что {@link Header} уникален в рамках системы
 * хотя бы в промежутке обработки запроса
 */
public class MessageRouter implements Observer<Response> {

    private static final Logger log = LogManager.getLogger();

    private Channel<Response> backwardChannel;
    private final Map<Header, Observer<Response>> messageCorrelations;
    private Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap;

    public MessageRouter(Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap,
                         Channel<Response> backwardChannel) {
        messageCorrelations = new ConcurrentHashMap<>();
        initDirectFlow(outgoingChannelsMap);
        initBackwardFlow(backwardChannel);
    }

    private void initDirectFlow(Map<Class<? extends Request>, DuplexChannel<Request, Response>> outgoingChannelsMap) {
        this.outgoingChannelsMap = new HashMap<>(CommonUtils.retunIfNotNull(outgoingChannelsMap));
        subscribeToAllOutgoingChannels();
    }

    private void initBackwardFlow(Channel<Response> backwardChannel) {
        this.backwardChannel = CommonUtils.retunIfNotNull(backwardChannel);
        this.backwardChannel.subscribe((rs) -> {
            log.info("Response received - {}", rs);
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
