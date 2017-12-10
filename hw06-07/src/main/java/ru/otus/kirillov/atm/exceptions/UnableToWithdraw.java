package ru.otus.kirillov.atm.exceptions;

/**
 * Created by Александр on 07.12.2017.
 */
public class UnableToWithdraw extends RuntimeException {

    public UnableToWithdraw(long summ) {
        super(String.format("Unable to withdraw %d available banknotes.", summ));
    }
}
