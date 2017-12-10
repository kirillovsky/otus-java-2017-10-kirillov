package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.commands.queries.Query;

/**
 * Created by Александр on 08.12.2017.
 */
public class UnknownCommand extends AbstractCommand{

    public UnknownCommand() {
        super(null);
    }

    @Override
    public Object execute(Query query) {
        throw new UnsupportedOperationException(
                "Unable to execute operation for message type - " + query.getType()
        );
    }
}
