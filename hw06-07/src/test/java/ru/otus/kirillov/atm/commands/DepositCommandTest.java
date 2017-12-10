package ru.otus.kirillov.atm.commands;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.DepositQuery;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.money.EURBanknote;
import ru.otus.kirillov.atm.money.RUBBanknote;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/** Комманда внесения средств в ATM
 * Created by Александр on 10.12.2017.
 */
public class DepositCommandTest {

    private CellManagement cellManagement;
    private Command<DepositQuery, Void> command;

    @Before
    public void initialize() {
        cellManagement = mock(CellManagement.class);
        command = new DepositCommand(cellManagement);
    }

    @Test
    public void testExecuteCallCheck() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 0),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(RUBBanknote.ONE_HUNDRED_RUBLES, 10),
                                Pair.of(EURBanknote.TEN_EURO, 4)
                        )
                );
        BillsPack billsPack = new BillsPack();
        billsPack.addBanknotes(RUBBanknote.FIFTY_RUBLES, 2);
        billsPack.addBanknotes(EURBanknote.TEN_EURO, 4);

        command.execute(new DepositQuery(billsPack));
        verify(cellManagement, times(2)).put(any(Banknote.class), anyInt());
        verify(cellManagement, atLeastOnce()).getBanknoteCountByType();
        verify(cellManagement, never()).get(any(Banknote.class), anyInt());
        verify(cellManagement, never()).restoreToInitial();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteNoBanknoteCellPresent() throws Exception {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 0),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(EURBanknote.TEN_EURO, 4)
                        )
                );

        BillsPack billsPack = new BillsPack();
        billsPack.addBanknotes(RUBBanknote.FIFTY_RUBLES, 2);
        billsPack.addBanknotes(EURBanknote.TWENTY_EURO, 4);

        try {
            command.execute(new DepositQuery(billsPack));
        } catch (Exception e) {
            verify(cellManagement, only()).getBanknoteCountByType();
            throw e;
        }
    }

    @Test
    public void testExecuteSomeBanknotes() {
        when(cellManagement.getBanknoteCountByType())
                .thenReturn(
                        Commons.ofMap(
                                Pair.of(RUBBanknote.FIFTY_RUBLES, 0),
                                Pair.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 2),
                                Pair.of(EURBanknote.TEN_EURO, 4),
                                Pair.of(RUBBanknote.TEN_RUBLES, 0),
                                Pair.of(EURBanknote.TWO_HUNDRED_EURO, 0)
                        )
                );

        BillsPack billsPack = new BillsPack();
        billsPack.addBanknotes(RUBBanknote.FIFTY_RUBLES, 1);
        billsPack.addBanknotes(EURBanknote.TEN_EURO, 2);
        billsPack.addBanknotes(RUBBanknote.FIVE_THOUSAND_RUBLES, 3);
        billsPack.addBanknotes(EURBanknote.TWO_HUNDRED_EURO, 4);

        command.execute(new DepositQuery(billsPack));

        verify(cellManagement, atLeastOnce()).getBanknoteCountByType();

        verify(cellManagement).put(eq(RUBBanknote.FIFTY_RUBLES), eq(1));
        verify(cellManagement).put(eq(RUBBanknote.FIVE_THOUSAND_RUBLES), eq(3));
        verify(cellManagement).put(eq(EURBanknote.TEN_EURO), eq(2));
        verify(cellManagement).put(eq(EURBanknote.TWO_HUNDRED_EURO), eq(4));
        verify(cellManagement, never()).put(eq(RUBBanknote.TEN_RUBLES), anyInt());

        verify(cellManagement, never()).get(any(Banknote.class), anyInt());
        verify(cellManagement, never()).restoreToInitial();
    }
}