package ru.otus.kirillov.atm.commands.queries;

import ru.otus.kirillov.atm.money.BillsPack;

/**
 * Запрос на снятие денежных средств
 * Created by Александр on 08.12.2017.
 */
public class DepositQuery extends Query {

    private BillsPack billsPack;

    public DepositQuery(BillsPack billsPack) {
        super(Type.DEPOSITING);
        this.billsPack = billsPack;
    }

    public BillsPack getBillsPack() {
        return billsPack;
    }
}
