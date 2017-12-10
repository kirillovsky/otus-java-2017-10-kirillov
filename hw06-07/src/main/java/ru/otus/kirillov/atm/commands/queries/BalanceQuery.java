package ru.otus.kirillov.atm.commands.queries;

/** Запрос на получение баланса банкомата по каждой валюте
 * Created by Александр on 07.12.2017.
 */
public class BalanceQuery extends Query{

    public BalanceQuery() {
        super(Type.BALANCE);
    }
}
