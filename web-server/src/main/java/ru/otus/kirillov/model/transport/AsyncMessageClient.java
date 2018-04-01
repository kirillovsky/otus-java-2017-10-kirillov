package ru.otus.kirillov.model.transport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.model.channels.DuplexChannel;
import ru.otus.kirillov.model.transport.Responses.ExceptionResponse;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Асинхронный клиент для взаимодействия с абстрактными каналами.
 * Реализация подразумевает, что каждый тип запроса можно отправить
 * только в определенный выходной канал. Ответ оборачивается в {@link CompletableFuture}.
 * Если с дуплексным каналом работает несколько клиентов, то о пришедшем сообщении нотифицируются все клиенты
 * При этом "чужие" сообщения будут отклонены
 * Опять же, данная реализация подразумевает, что {@link Header} уникален в рамках системы
 * хотя бы в промежутке обработки запроса
 * @param <RQ> - тип входного запроса (заголовок которого испоьзуется для квитовки ответов)
 * @param <RS> - тип ответа
 */
public class AsyncMessageClient<RQ extends Message, RS extends Message> implements Observer<RS> {

    private static final Logger log = LogManager.getLogger();

    private final Map<Header, CompletableFuture<RS>> messageCorrelations;
    private Map<Class<? extends Message>, DuplexChannel<RQ, RS>> outgoingChannelsMap;

    public AsyncMessageClient(Map<Class<? extends Message>, DuplexChannel<RQ, RS>> outgoingChannelsMap) {
        messageCorrelations = new ConcurrentHashMap<>();
        initDirectFlow(outgoingChannelsMap);
    }

    private void initDirectFlow(Map<Class<? extends Message>, DuplexChannel<RQ, RS>> outgoingChannelsMap) {
        this.outgoingChannelsMap = new HashMap<>(CommonUtils.retunIfNotNull(outgoingChannelsMap));
        subscribeToAllOutgoingChannels();
    }

    private void subscribeToAllOutgoingChannels() {
        outgoingChannelsMap.forEach((clazz, channel) -> channel.subscribe(this));
    }

    public CompletableFuture<RS> send(RQ rq) {
        //1. Ищем канал если не нашли, кидаем экспешн
        DuplexChannel<RQ, RS> channel = outgoingChannelsMap.computeIfAbsent(rq.getClass(), (c) -> {
            throw new RuntimeException("Not found channel for incoming request type - " + rq.getClass());
        });
        CompletableFuture<RS> asyncResponse = new CompletableFuture<>();
        //2. Сохраняем корреляцию запроса
        messageCorrelations.put(rq.getHeader(), asyncResponse);
        //3. А теперь можно отправлять
        channel.send(rq);
        return asyncResponse;
    }

    @Override
    public void receive(RS rs) {
        log.info("Response received - {}", rs);
        Header rsHeader = rs.getHeader();
        CompletableFuture<RS> asyncResponse = messageCorrelations.remove(rsHeader);
        if (asyncResponse == null) {
            log.info("Not found correlation for response header - {}. Response will reject.", rsHeader);
            return;
        }
        if (rs instanceof ExceptionResponse) {
            log.warn("Received exception in response - {}", rs);
            asyncResponse.completeExceptionally(((ExceptionResponse)rs).getException());
        } else {
            asyncResponse.complete(rs);
        }
    }
}
