package ru.otus.kirillov.atm.commands.queries;

import ru.otus.kirillov.atm.currency.Currency;

/** Запрос на снятие наличных.
 * Created by Александр on 05.12.2017.
 */
public class WithdrawQuery extends Query  {

    /**
     * Валюта
     */
    private final Currency currency;

    /**
     * Сумма в заданной валюте
     */
    private final long sum;

    public WithdrawQuery(Currency currency, long sum) {
        super(Type.WITHRDAWAL);
        this.currency = currency;
        this.sum = sum;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getSum() {
        return sum;
    }
}
