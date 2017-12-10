package ru.otus.kirillov.atm.money;

import ru.otus.kirillov.atm.currency.Currency;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/** Рублевые банкноты.
 * Именно enum, так как банкноты могут быть премиальными или коллекционными,
 * хотя и иметь один номинал. Это может пригодиться для бизнес логики
 * Created by Александр on 03.12.2017.
 */
public enum RUBBanknote implements Banknote{

    TEN_RUBLES(10),
    FIFTY_RUBLES(50),
    ONE_HUNDRED_RUBLES(100),
    TWO_HUNDRED_RUBLES(200),
    FIVE_HUNDRED_RUBLES(500),
    ONE_THOUSAND_RUBLES(1000),
    TWO_THOUSAND_RUBLES(2000),
    FIVE_THOUSAND_RUBLES(5000);

    private long nominal;

    RUBBanknote(long nominal) {
        this.nominal = nominal;
    }

    public Currency getCurrency() {
        return Currency.RUB;
    }

    public long getNominal() {
        return nominal;
    }
}
