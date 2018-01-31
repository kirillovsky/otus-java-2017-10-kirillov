package ru.otus.kirillov.myorm.commands;

import java.sql.Connection;

/**
 * Created by Александр on 30.01.2018.
 */
public abstract class AbstractCommand<T extends Request, R> implements Command<T, R> {

    private CommandInvoker invoker;

    public AbstractCommand(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    protected CommandInvoker getInvoker() {
        return invoker;
    }

    protected Connection getConnection() {
        return getInvoker().getConnection();
    }
}
