package ru.otus.kirillov.atm.commands;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.commands.queries.WithdrawQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.exceptions.UnableToWithdraw;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.money.EURBanknote;
import ru.otus.kirillov.atm.money.RUBBanknote;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Команда на выдачу средств
 * Created by Александр on 10.12.2017.
 */
public class WithdrawCommandTest {

    private CellManagement cellManagement;
    private Command<WithdrawQuery, BillsPack> command;

    @Before
    public void initialize() {
        cellManagement = mock(CellManagement.class);
        command = new WithdrawCommand(cellManagement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCurrencyNotSupported() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 0),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10)
                        )
                );

        command.execute(new WithdrawQuery(Currency.EURO, 1_000L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWrongSum() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 0),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10)
                        )
                );

        command.execute(new WithdrawQuery(Currency.RUB, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteTooBigSum() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 10),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(EURBanknote.TEN_EURO, 5)
                        )
                );

        command.execute(new WithdrawQuery(Currency.RUB, 50 * 10L + 5_000 * 2L + 100 * 10L + 1));
    }

    @Test(expected = UnableToWithdraw.class)
    public void testExecuteNoBanknoteForSum() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 10),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(EURBanknote.TEN_EURO, 5)
                        )
                );

        command.execute(new WithdrawQuery(Currency.RUB, 2_000L));
    }

    @Test(expected = UnableToWithdraw.class)
    public void testExecuteNotEnoughForSum() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.TEN_RUBLES, 3),
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 10),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(EURBanknote.TEN_EURO, 5)
                        )
                );

        command.execute(new WithdrawQuery(Currency.RUB, 5_040L));
    }

    @Test
    public void testExecuteGetSomeBanknotes() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.TEN_RUBLES, 3),
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 10),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(RUBBanknote.FIVE_HUNDRED_RUBLES, 3),
                                Pair.of(EURBanknote.TEN_EURO, 5)
                        )
                );

        BillsPack billsPack = command.execute(new WithdrawQuery(Currency.RUB, 5_230L));
        Assert.assertEquals(billsPack.getCountsByType(),
                Commons.ofMap(
                        Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 1),
                        Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 2),
                        Pair.of(RUBBanknote.TEN_RUBLES, 3)
                )
        );

        verify(cellManagement).get(eq(RUBBanknote.TEN_RUBLES), eq(3));
        verify(cellManagement).get(eq(RUBBanknote.ONE_HUNDRED_RUBLES), eq(2));
        verify(cellManagement).get(eq(RUBBanknote.FIVE_THOUSAND_RUBLES), eq(1));

        verify(cellManagement, never()).get(eq(RUBBanknote.FIFTY_RUBLES), anyInt());
        verify(cellManagement, never()).get(eq(RUBBanknote.FIVE_HUNDRED_RUBLES), anyInt());
        verify(cellManagement, never()).get(eq(EURBanknote.TEN_EURO), anyInt());

        verify(cellManagement, atLeastOnce()).getBanknoteCountByType();
        verify(cellManagement, never()).put(any(Banknote.class), anyInt());
        verify(cellManagement, never()).restoreToInitial();
    }

    @Test
    public void testExecuteGetMinimumBanknotes() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.TEN_RUBLES, 100),
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 100),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 100),
                                Pair.of(RUBBanknote.TWO_HUNDRED_RUBLES, 100),
                                Pair.of(RUBBanknote.FIVE_HUNDRED_RUBLES, 100),
                                Pair.of(RUBBanknote.ONE_THOUSAND_RUBLES, 100),
                                Pair.of(RUBBanknote.TWO_THOUSAND_RUBLES, 100),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 100),
                                Pair.of(EURBanknote.TEN_EURO, 100)
                        )
                );

        BillsPack billsPack = command.execute(new WithdrawQuery(Currency.RUB, 8_860L));
        Assert.assertEquals(billsPack.getCountsByType(),
                Commons.ofMap(
                        Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 1),
                        Pair.of(RUBBanknote.TWO_THOUSAND_RUBLES, 1),
                        Pair.of(RUBBanknote.ONE_THOUSAND_RUBLES, 1),
                        Pair.of(RUBBanknote.FIVE_HUNDRED_RUBLES, 1),
                        Pair.of(RUBBanknote.TWO_HUNDRED_RUBLES, 1),
                        Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 1),
                        Pair.of(RUBBanknote.FIFTY_RUBLES, 1),
                        Pair.of(RUBBanknote.TEN_RUBLES, 1)
                )
        );

        for(Banknote b: RUBBanknote.values()) {
            verify(cellManagement).get(eq(b), eq(1));
        }
        verify(cellManagement, never()).get(eq(EURBanknote.TEN_EURO), anyInt());

        verify(cellManagement, atLeastOnce()).getBanknoteCountByType();
        verify(cellManagement, never()).put(any(Banknote.class), anyInt());
        verify(cellManagement, never()).restoreToInitial();
    }

}