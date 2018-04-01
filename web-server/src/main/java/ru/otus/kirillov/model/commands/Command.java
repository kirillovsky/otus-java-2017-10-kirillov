package ru.otus.kirillov.model.commands;

/**
 * Интерфейс для всех комманд системы
 */
public interface Command {

    boolean isApplicable(ModelRequest rq);

    /**
     * Выполнить команду
     * @param rq - запрос
     * @return результат выполнения
     */
    ModelResult execute(ModelRequest rq);
}
