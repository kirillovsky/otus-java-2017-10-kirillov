package ru.otus.kirillov.model.channels;

import ru.otus.kirillov.model.Observable;

/**
 * Интерфейс асинхронного обработчика дуплексного канала связи.
 * @param <RQ> - тип запроса
 * @param <RS> - тип ответа
 */
public interface DuplexChannel<RQ, RS> extends Observable<RS> {

    /**
     * Отправка сообщения
     * @param response
     */
    void send(RQ response);
}
