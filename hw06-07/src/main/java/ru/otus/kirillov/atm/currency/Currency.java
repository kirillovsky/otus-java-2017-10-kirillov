package ru.otus.kirillov.atm.currency;

import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.EURBanknote;
import ru.otus.kirillov.atm.money.RUBBanknote;

import java.util.Arrays;
import java.util.List;

/** Валюты!
 * Created by Александр on 03.12.2017.
 */
public enum Currency {
    RUB(RUBBanknote.class),
    EURO(EURBanknote.class);

    Class<? extends Banknote> banknoteType;

    Currency(Class<? extends Banknote> banknoteType) {
        this.banknoteType = banknoteType;
    }

    public List<Banknote> getBanknoteType() {
        return Arrays.asList(banknoteType.getEnumConstants());
    }
}
