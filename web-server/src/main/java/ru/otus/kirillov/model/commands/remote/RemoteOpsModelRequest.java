package ru.otus.kirillov.model.commands.remote;

import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.transport.Requests.RemoteOperation;
import ru.otus.kirillov.utils.CommonUtils;

/** Еще одна комманда для модели. Данная комманда предназначена
 * для асинхронного выполнения удаленных вызовов и их обработки в
 * коллбеке.
 * @param <S> - тип удаленного сервиса
 * @param <R> - тип результата
 */
public class RemoteOpsModelRequest<S, R> implements ModelRequest {

    private final RemoteOperation<S, R> request;

    public RemoteOpsModelRequest(RemoteOperation<S, R> request) {
        this.request = CommonUtils.retunIfNotNull(request);
    }

    public RemoteOperation<S, R> getRequest() {
        return request;
    }
}
