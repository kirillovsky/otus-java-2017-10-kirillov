package ru.otus.kirillov.atm.cells;

import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.Banknote;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * "Простая" фабрика создания CellManagement
 * (по терминологии refactoring.guru)
 * Created by Александр on 08.12.2017.
 */
public class CellManagementSimpleFactory {

    public static final int DEFAULT_BANKNOTES_PER_TYPE_COUNT = 100;

    public static CellManagement createSimpleForCurrency(Currency currency) {
        return createSimpleForCurrency(currency, DEFAULT_BANKNOTES_PER_TYPE_COUNT);
    }

    public static CellManagement createSimpleForCurrency(Currency currency, int count) {
        return create(currency.getBanknoteType().stream()
                .collect(Collectors.toMap(Function.identity(), i -> count))
        );
    }

    public static CellManagement create(Map<Banknote, Integer> banknoteCountMap) {
        return new CellRack(
                banknoteCountMap.entrySet().stream()
                        .collect(Collectors.mapping(
                                e -> new Cell(e.getKey(), e.getValue()),
                                Collectors.toList())
                        )
        );
    }
}
