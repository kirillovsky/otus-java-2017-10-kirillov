package ru.otus.kirillov.atm.commands;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.EURBanknote;
import ru.otus.kirillov.atm.money.RUBBanknote;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;

/** Тест для комманды внесения средств
 * Created by Александр on 10.12.2017.
 */
public class BalanceCommandTest {

    private CellManagement cellManagement;
    private Command<BalanceQuery, Map<Currency, Long>> command;

    @Before
    public void initialize() {
        cellManagement = mock(CellManagement.class);
        command = new BalanceCommand(cellManagement);
    }

    @Test
    public void testExecuteCallCheck() throws Exception {
        command.execute(new BalanceQuery());
        verify(cellManagement, atLeastOnce()).getBanknoteCountByType();
        verify(cellManagement, never()).get(any(Banknote.class), anyInt());
        verify(cellManagement, never()).put(any(Banknote.class), anyInt());
        verify(cellManagement, never()).restoreToInitial();
    }

    @Test
    public void testExecuteOneBanknoteType() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(Collections.singletonMap(RUBBanknote.ONE_HUNDRED_RUBLES, 111));
        Assert.assertEquals(command.execute(new BalanceQuery()),
                Collections.singletonMap(Currency.RUB, 111 * 100L)
        );
    }

    @Test
    public void testExecuteOneCurrency() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(RUBBanknote.TEN_RUBLES, 12),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 3),
                                Pair.of(RUBBanknote.TWO_HUNDRED_RUBLES, 0)
                        ));
        Assert.assertEquals(command.execute(new BalanceQuery()),
                Collections.singletonMap(Currency.RUB, 10 * 100L + 12 * 10L + 3 * 5_000L)
        );
    }

    @Test
    public void testExecuteSomeCurrencies() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 6),
                                Pair.of(RUBBanknote.ONE_THOUSAND_RUBLES, 11),
                                Pair.of(EURBanknote.TWO_HUNDRED_EURO, 100),
                                Pair.of(EURBanknote.FIFTY_EURO, 3)
                        ));
        Assert.assertEquals(command.execute(new BalanceQuery()),
                Commons.ofMap(
                        Pair.of(Currency.EURO, 200 * 100L + 50 * 3L),
                        Pair.of(Currency.RUB, 1_000 * 11L + 5_000 * 6L)
                )
        );
    }

    @Test
    public void testExecuteEmptyCells() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 6),
                                Pair.of(RUBBanknote.ONE_THOUSAND_RUBLES, 11),
                                Pair.of(EURBanknote.TWO_HUNDRED_EURO, 0),
                                Pair.of(EURBanknote.FIFTY_EURO, 0)
                        ));
        Assert.assertEquals(command.execute(new BalanceQuery()),
                Commons.ofMap(
                        Pair.of(Currency.EURO, 0L),
                        Pair.of(Currency.RUB, 1_000 * 11L + 5_000 * 6L)
                )
        );
    }
}