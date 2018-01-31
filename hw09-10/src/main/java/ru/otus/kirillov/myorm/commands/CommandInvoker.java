package ru.otus.kirillov.myorm.commands;

import ru.otus.kirillov.myorm.commands.delete.DeleteCommand;
import ru.otus.kirillov.myorm.commands.generatesql.GenerateSqlCommand;
import ru.otus.kirillov.myorm.commands.select.SelectCommand;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Александр on 30.01.2018.
 */
public class CommandInvoker {

    protected static class UnknownCommand implements Command {
        @Override
        public Object execute(Request request) {
            throw new UnsupportedOperationException(
                    "Not found command for request type: " + request.getType()
            );
        }
    }

    private static Command UNKNOWN_COMMAND_INSTACE = new UnknownCommand();

    private Map<Request.Type, Command> commandsMap = new HashMap<>();

    private CommandInvoker() {
        addCommand(Request.Type.GENERATE_SQL, new GenerateSqlCommand(this));
        addCommand(Request.Type.SELECT, new SelectCommand(this));
        addCommand(Request.Type.DELETE, new DeleteCommand(this));
    }

    public CommandInvoker(Connection connection) {
        this();
        this.connection = connection;
    }

    public <RQ extends Request, RS> RS invoke(RQ request) {
        Command<RQ, RS> command = commandsMap.getOrDefault(request.getType(), UNKNOWN_COMMAND_INSTACE);
        return command.execute(request);
    }

    public <RQ extends Request, RS> void addCommand(Request.Type type, Command<RQ, RS> command) {
        commandsMap.put(type, command);
    }

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }
}
