package ru.otus.kirillov.model.commands;

import ru.otus.kirillov.model.commands.Request;
import ru.otus.kirillov.model.commands.Result;

/**
 * Интерфейс для всех комманд системы
 */
public interface Command {

    boolean isApplicable(Request rq);

    /**
     * Выполнить команду
     * @param rq - запрос
     * @return результат выполнения
     */
    Result execute(Request rq);
}
