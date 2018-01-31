package ru.otus.kirillov.myorm.commands;

import ru.otus.kirillov.utils.CommonUtils;

/**
 * Created by Александр on 30.01.2018.
 */
public abstract class Request {

    public enum Type {
        GENERATE_SQL,
        SELECT,
        INSERT,
        UPDATE,
        SAVE_OR_UPDATE,
        DELETE
    }

    private Type type;

    private CommandInvoker invoker;

    protected Request(Type type) {
        this.type = CommonUtils.retunIfNotNull(type);
    }

    public Type getType() {
        return type;
    }

    public CommandInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(CommandInvoker invoker) {
        this.invoker = invoker;
    }
}
