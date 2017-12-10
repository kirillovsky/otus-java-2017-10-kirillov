package ru.otus.kirillov.atm.commands;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.atm.exceptions.UnableToWithdraw;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.WithdrawQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.List;
import java.util.Set;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Команда на выдачу денег банкоматом
 * Created by Александр on 06.12.2017.
 * @see ru.otus.kirillov.atm.ATM#withdraw(Currency, long)
 */
public class WithdrawCommand extends AbstractCommand<WithdrawQuery, BillsPack> {

    public WithdrawCommand(CellManagement cellManagement) {
        super(cellManagement);
    }

    @Override
    public BillsPack execute(WithdrawQuery query) {
        checkParams(query);
        checkExecutionPossibility(query);
        return withdraw(query);
    }

    private void checkParams(WithdrawQuery query) {
        Commons.requiredNotNull(getATMCurrencies(getCellManagement()).contains(query.getCurrency()),
                "ATM no supported currency: " + query.getCurrency());
        Commons.requiredTrue(query.getSum() > 0 &&
                        query.getSum() <= getATMSum(query.getCurrency(), getCellManagement()),
                "Withdraw sum must be in interval (0, atmSum(query.currency)]");
    }

    private long getATMSum(Currency currency, CellManagement cellManagement) {
        return getBanknoteStream(currency, cellManagement)
                .collect(Collectors.summingLong(Banknote::getSum));
    }

    private Set<Currency> getATMCurrencies(CellManagement cellManagement) {
        return cellManagement.getBanknoteCountByType().entrySet().stream()
                .map(e -> e.getKey().getCurrency())
                .collect(Collectors.toSet());

    }

    private void checkExecutionPossibility(WithdrawQuery query) {
        if (withdrawCalc(query) != 0) {
            throw new UnableToWithdraw(query.getSum());
        }
    }

    private BillsPack withdraw(WithdrawQuery query) {
        BillsPack result = new BillsPack();
        withdrawCalc(query,
                (b, i) -> {
                    getCellManagement().get(b, i);
                    result.addBanknotes(b, i);
                });
        return result;
    }

    private Stream<Pair<Banknote, Integer>> getBanknoteStream(Currency currency, CellManagement cellManagement) {
        return cellManagement.getBanknoteCountByType().entrySet().stream()
                .filter(b -> b.getKey().getCurrency() == currency)
                .map(e -> Pair.of(e.getKey(), e.getValue()))
                .filter(e -> e.getValue() > 0);
    }

    private long withdrawCalc(WithdrawQuery query) {
        return withdrawCalc(query, (b, i) -> {});
    }

    private long withdrawCalc(WithdrawQuery query, ObjIntConsumer<Banknote> withdrawSucceedConsumer) {
        List<Pair<Banknote, Integer>> sortedBanknotePair =
                getBanknoteStream(query.getCurrency(), getCellManagement())
                        .sorted(Banknote.comparatorByNominalForPair())
                        .collect(Collectors.toList());

        long retainSum = query.getSum();

        for (Pair<Banknote, Integer> pair : sortedBanknotePair) {
            int ejectedBanknotesCount =
                    Math.min(Banknote.getBanknoteCountForSum(pair.getKey(), retainSum), pair.getRight());

            if(ejectedBanknotesCount == 0)
                continue;
            retainSum -= Banknote.getSum(pair.getKey(), ejectedBanknotesCount);

            withdrawSucceedConsumer.accept(pair.getKey(), ejectedBanknotesCount);

            if (retainSum == 0) {
                break;
            }
        }
        return retainSum;
    }
}
