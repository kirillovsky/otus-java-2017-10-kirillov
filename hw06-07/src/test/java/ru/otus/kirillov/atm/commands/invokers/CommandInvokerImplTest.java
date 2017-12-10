package ru.otus.kirillov.atm.commands.invokers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.Command;
import ru.otus.kirillov.atm.commands.UnknownCommand;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.commands.queries.Query;
import ru.otus.kirillov.atm.commands.queries.UndoToDefaultQuery;
import ru.otus.kirillov.atm.commands.queries.WithdrawQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестируем маршрутизацию запросов к нужным коммандам
 * Created by Александр on 10.12.2017.
 */
public class CommandInvokerImplTest {

    @Test
    public void testProcess() throws Exception {
        Map<Currency, Long> expectedResult = Collections.singletonMap(Currency.EURO, 1000L);

        Command<BalanceQuery, Map<Currency, Long>> command = mock(Command.class);
        when(command.execute(any(BalanceQuery.class)))
                .thenReturn(expectedResult);

        CommandInvoker invoker = new CommandInvokerImpl(mock(CellManagement.class),
                Commons.ofMap(
                        Pair.of(Query.Type.BALANCE, c -> command),
                        Pair.of(Query.Type.DEPOSITING, c -> new UnknownCommand())
                )
        );

        Assert.assertEquals(invoker.process(new BalanceQuery()), expectedResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProcessNegative() throws Exception {
        Map<Currency, Long> expectedResult = Collections.singletonMap(Currency.EURO, 1000L);

        Command<BalanceQuery, Map<Currency, Long>> command = mock(Command.class);
        when(command.execute(any(BalanceQuery.class)))
                .thenReturn(expectedResult);

        CommandInvoker invoker = new CommandInvokerImpl(mock(CellManagement.class),
                Commons.ofMap(
                        Pair.of(Query.Type.BALANCE, c -> command)
                )
        );

        invoker.process(new UndoToDefaultQuery());
    }

    @Test
    public void testAddCommand() throws Exception {
        Command<WithdrawQuery, BillsPack> command = mock(Command.class);
        BillsPack billsPack = new BillsPack();
        when(command.execute(any(WithdrawQuery.class)))
                .thenReturn(billsPack);

        CommandInvoker invoker = new CommandInvokerImpl(mock(CellManagement.class), Collections.emptyMap());
        invoker.addCommand(Query.Type.WITHRDAWAL, command);
        invoker.addCommand(Query.Type.DEPOSITING, mock(Command.class));

        Assert.assertEquals(billsPack, invoker.process(new WithdrawQuery(Currency.EURO, 1_000L)));
        verify(command).execute(any(WithdrawQuery.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddCommandNegative() throws Exception {
        Command<WithdrawQuery, BillsPack> command = mock(Command.class);
        BillsPack billsPack = new BillsPack();
        when(command.execute(any(WithdrawQuery.class)))
                .thenReturn(billsPack);

        CommandInvoker invoker = new CommandInvokerImpl(mock(CellManagement.class), Collections.emptyMap());
        invoker.addCommand(Query.Type.WITHRDAWAL, command);
        invoker.process(new BalanceQuery());
    }
}