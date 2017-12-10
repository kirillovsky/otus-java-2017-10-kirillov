package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.commands.queries.DepositQuery;

/**
 * Created by Александр on 08.12.2017.
 */
public class DepositCommand implements Command<DepositQuery, Void> {

    @Override
    public Void execute(DepositQuery query) {
        return null;
    }
}
