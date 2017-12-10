package ru.otus.kirillov.atm.money;

import ru.otus.kirillov.atm.currency.Currency;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Александр on 08.12.2017.
 */
public enum  EURBanknote implements Banknote {

    FIVE_EURO(5),
    TEN_EURO(10),
    TWENTY_EURO(20),
    FIFTY_EURO(50),
    TWO_HUNDRED_EURO(200),
    FIVE_HUNDRED_EURO(500);

    private long nominal;

    EURBanknote(long nominal) {
        this.nominal = nominal;
    }

    @Override
    public Currency getCurrency() {
        return Currency.EURO;
    }

    @Override
    public long getNominal() {
        return nominal;
    }
}
