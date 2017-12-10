package ru.otus.kirillov.atmdepartment;

import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.currency.Currency;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Александр on 09.12.2017.
 */
public interface ATMDepartment {

    /**
     * Получить баланс по всем валютам всех ATM.
     * @return - ассоциативный массив (валюта, сумма баланса)
     */
    Map<Currency, Long> getAllBalance();

    /**
     * Откат всех банкоматов до дефолтного состояния.
     * Откат банкоматов происходит независимо.
     * т.е. если при попытке отката произошло исключение
     * на одном или нескольких банкоматах, то система будет пытаться откатить остальные.
     * @throws ru.otus.kirillov.atmdepartment.exception.UndoNamedATMException -
     * со списком имен банкоматов, для которых откат прошел неудачно
     */
    void restoreAllToDefaultState();

    /**
     * Добавить ATM.
     * @param atm
     * @throws IllegalArgumentException если {@param atm} == null
     */
    void addATM(ATM atm);

    /**
     * Добавить несколько ATM.
     * @param atms
     * @throws IllegalArgumentException если {@param atms} == null
     */
    void addATMs(Collection<ATM> atms);
}
