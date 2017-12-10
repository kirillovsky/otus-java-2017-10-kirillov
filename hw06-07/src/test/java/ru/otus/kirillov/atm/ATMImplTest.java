package ru.otus.kirillov.atm;

import org.junit.Test;
import ru.otus.kirillov.atm.commands.invokers.CommandInvoker;
import ru.otus.kirillov.atm.commands.queries.BalanceQuery;
import ru.otus.kirillov.atm.commands.queries.DepositQuery;
import ru.otus.kirillov.atm.commands.queries.UndoToDefaultQuery;
import ru.otus.kirillov.atm.commands.queries.WithdrawQuery;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.BillsPack;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/** С учетом реализации было трудно представить, что тестировать.
 * Но я попытался :)
 * Created by Александр on 10.12.2017.
 */
public class ATMImplTest {

    @Test
    public void testDeposit() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        atm.deposit(new BillsPack());
        verify(commandInvoker).process(any(DepositQuery.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositNegative() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        doThrow(IllegalArgumentException.class).when(commandInvoker).process(any(DepositQuery.class));
        atm.deposit(new BillsPack());
    }

    @Test
    public void testGetBalance() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        atm.getBalance();
        verify(commandInvoker).process(any(BalanceQuery.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBalanceNegative() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        doThrow(IllegalArgumentException.class).when(commandInvoker).process(any(BalanceQuery.class));
        atm.getBalance();
    }

    @Test
    public void testWithdraw() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        atm.withdraw(Currency.EURO, 100L);
        verify(commandInvoker).process(any(WithdrawQuery.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawNegative() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        doThrow(IllegalArgumentException.class).when(commandInvoker).process(any(WithdrawQuery.class));
        atm.withdraw(Currency.EURO, 100L);
    }

    @Test
    public void testRestoreToDefaultState() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        atm.restoreToDefaultState();
        verify(commandInvoker).process(any(UndoToDefaultQuery.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestoreToDefaultStateNegative() throws Exception {
        CommandInvoker commandInvoker = mock(CommandInvoker.class);
        ATM atm = new ATMImpl(commandInvoker);
        doThrow(IllegalArgumentException.class).when(commandInvoker).process(any(UndoToDefaultQuery.class));
        atm.restoreToDefaultState();
    }

}