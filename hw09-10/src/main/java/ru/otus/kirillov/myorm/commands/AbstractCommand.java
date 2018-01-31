package ru.otus.kirillov.myorm.commands;

import java.sql.Connection;
import java.util.function.Consumer;

/**
 * Created by Александр on 30.01.2018.
 */
public abstract class AbstractCommand<T extends Request, R> implements Command<T, R> {

    protected interface ConnectionOperationExecutor {
        void execute(Connection con) throws Exception;
    }

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

    protected void unhandled(Connection con, ConnectionOperationExecutor op) {
        try {
            op.execute(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
