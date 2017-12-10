package ru.otus.kirillov.atm;

import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.exceptions.UnableToWithdraw;
import ru.otus.kirillov.atm.money.BillsPack;

import java.util.Map;

/**
 * Интерфейс банкомата.
 * Created by Александр on 08.12.2017.
 */
public interface ATM {

    /**
     * Внести деньги в АТМ
     *
     * @param billsPack - пачка купюр
     * @throws IllegalArgumentException - если:
     *                                  1. ATM не поддерживает внесение купюр данного типа
     */
    void deposit(BillsPack billsPack);

    /**
     * Получить баланс по всем валютам ATM.
     *
     * @return - ассоциативный массив (валюта, сумма баланса)
     */
    Map<Currency, Long> getBalance();

    /**
     * Выдача купюр.
     *
     * @param currency - валюта
     * @param sum      - сумма
     * @return пачка купюр
     * @throws IllegalArgumentException - если:
     *                                  1. ATM не поддерживает выдачу валюты {@param currency}
     *                                  2. если сумма выдачи не пренадлежит интервалу (0, atmSum(currency)]
     * @throws UnableToWithdraw         - если выдача не возможна,
     *                                  так как в ATM недостаточно купюр для ее выдачи
     */
    BillsPack withdraw(Currency currency, long sum);

    /**
     * Откат банкомата до дефолтного состояния
     */
    void restoreToDefaultState();
}
