package ru.otus.kirillov.atm.commands.queries;

import ru.otus.kirillov.atm.cells.CellManagement;

/**
 * Запрос на снятие денежных средств
 * Created by Александр on 08.12.2017.
 */
public class DepositQuery extends Query {

    public DepositQuery(CellManagement cellManagement) {
        super(cellManagement, Type.DEPOSITING);
    }
}
