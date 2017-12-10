package ru.otus.kirillov.atm;

import ru.otus.kirillov.atm.commands.invokers.CommandInvoker;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.commands.queries.DepositQuery;
import ru.otus.kirillov.atm.commands.queries.UndoToDefaultQuery;
import ru.otus.kirillov.atm.commands.queries.WithdrawQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Map;

/** @see ATM
 * Created by Александр on 09.12.2017.
 */
public class ATMImpl implements ATM {

    private CommandInvoker invoker;

    public ATMImpl(CommandInvoker invoker) {
        Commons.requiredNotNull(invoker, "command invoker must be not null");
        this.invoker = invoker;
    }

    @Override
    public void deposit(BillsPack billsPack) {
        invoker.process(new DepositQuery(billsPack));
    }

    @Override
    public Map<Currency, Long> getBalance() {
        return invoker.process(new BalanceQuery());
    }

    @Override
    public BillsPack withdraw(Currency currency, long sum) {
        return invoker.process(new WithdrawQuery(currency, sum));
    }

    @Override
    public void restoreToDefaultState() {
        invoker.process(new UndoToDefaultQuery());
    }
}
