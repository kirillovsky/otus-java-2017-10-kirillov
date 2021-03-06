package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.DepositQuery;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.utils.Commons;

/**
 * Внесение денег в банкомат
 * @see ru.otus.kirillov.atm.ATM#deposit(BillsPack)
 * Created by Александр on 08.12.2017.
 */
public class DepositCommand extends AbstractCommand<DepositQuery, Void> {

    public DepositCommand(CellManagement cellManagement) {
        super(cellManagement);
    }

    @Override
    public Void execute(DepositQuery query) {
        CellManagement management = getCellManagement();
        BillsPack billsPack = query.getBillsPack();

        checkBillsPack(management, billsPack);
        billsPack.getCountsByType().forEach(management::put);
        return null;
    }

    private void checkBillsPack(CellManagement cellManagement, BillsPack billsPack) {
        Commons.requiredTrue(
                cellManagement.getBanknoteCountByType().keySet().containsAll(billsPack.getBanknotesType()),
                "Deposit of entered banknotes not supported for this ATM");
    }
}
