package ru.otus.kirillov.model.channels;

import ru.otus.kirillov.transport.Observable;

/**
 * Однонаправленный канал с асинхронной обработкой
 * @param <T>
 */
public interface Channel<T> extends Observable<T> {
    /**
     * Отправка сообщения
     * @param msg
     */
    void send(T msg);
}
