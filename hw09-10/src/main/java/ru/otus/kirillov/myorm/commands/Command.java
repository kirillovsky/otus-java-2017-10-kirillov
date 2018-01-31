package ru.otus.kirillov.myorm.commands;

/**
 * Created by Александр on 30.01.2018.
 */
public interface Command<T extends Request, R> {

    R execute(T request);
}
