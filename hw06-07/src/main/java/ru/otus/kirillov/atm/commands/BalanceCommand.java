package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.Banknote;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Получение сводки баланска по всем валютам в банкомате
 * Created by Александр on 07.12.2017.
 */
public class BalanceCommand extends AbstractCommand<BalanceQuery, Map<Currency, Long>> {

    public BalanceCommand(CellManagement cellManagement) {
        super(cellManagement);
    }

    @Override
    public Map<Currency, Long> execute(BalanceQuery query) {
        return getCellManagement().getBanknoteCountByType().entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().getCurrency(),
                        Collectors.summingLong(e -> Banknote.getSum(e.getKey(), e.getValue()))
                        )
                );
    }
}
