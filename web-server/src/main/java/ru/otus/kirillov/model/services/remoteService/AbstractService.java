package ru.otus.kirillov.model.services.remoteService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.Observer;
import ru.otus.kirillov.model.transport.Header;
import ru.otus.kirillov.model.transport.MessageClient;
import ru.otus.kirillov.model.transport.Responses;
import ru.otus.kirillov.model.transport.Responses.ExceptionResponse;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.model.transport.Responses.ValueResponse;
import ru.otus.kirillov.utils.CommonUtils;
import static ru.otus.kirillov.model.transport.Requests.*;

/**
 * Абстракция для удаленных сервисов
 * @param <S> - тип удаленного сервиса
 */
public abstract class AbstractService<S> implements Observer<Request> {

    private static final Logger log = LogManager.getLogger();

    private final S service;
    private final MessageClient<Response, Request> client;

    public AbstractService(S service, MessageClient<Response, Request> client) {
        this.service = CommonUtils.retunIfNotNull(service);
        this.client = CommonUtils.retunIfNotNull(client);
        client.subscribe(this);
    }

    @Override
    public void receive(Request rq) {
        log.debug("Receive rq - {}", rq);
        try {
            RemoteOperation<S, ?> actualRq = parseRequest(rq);
            Object result = actualRq.getRemoteOp().apply(service);
            sendSuccessResult(result, rq.getHeader());
        } catch (Throwable e) {
            log.warn("Error occurred - {}", e);
            sendException(e, rq.getHeader());
        }
    }

    @SuppressWarnings("unchecked")
    private RemoteOperation<S, ?> parseRequest(Request rq) {
        if (!(rq instanceof RemoteOperation)) {
            throw new RuntimeException("Illegal request received. " + rq);
        }
        return (RemoteOperation<S, ?>)rq;
    }

    @SuppressWarnings("unchecked")
    private void sendSuccessResult(Object result, Header header) {
        client.send(new ValueResponse(result, header));
        log.debug("Sent success result - {}", result);
    }

    private void sendException(Throwable e, Header header) {
        client.send(new ExceptionResponse<>(e, header));
        log.warn("Sent exception result - {}", e);
    }
}
