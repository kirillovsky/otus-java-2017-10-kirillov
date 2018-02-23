package ru.otus.kirillov.model.commands;

import ru.otus.kirillov.model.request.Request;
import ru.otus.kirillov.model.result.Result;

/**
 * Интерфейс для всех комманд системы
 * @param <RQ> - запрос
 * @param <RS> - результат выполнения
 */
public interface Command<RQ extends Request, RS extends Result> {

    /**
     * Выполнить команду
     * @param rq - запрос
     * @return результат выполнения
     */
    RS execute(RQ rq);
}
