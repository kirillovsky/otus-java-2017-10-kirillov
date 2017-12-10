package ru.otus.kirillov.atmdepartment;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.utils.Commons;
import ru.otus.kirillov.atmdepartment.exception.UndoNamedATMException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Александр on 10.12.2017.
 */
public class NamedATMDepartmentTest {

    @Test
    public void testGetAllBalanceForEmptyATMs() {
        Assert.assertEquals("Wrong summary of atms balance in department",
                Collections.emptyMap(),
                new NamedATMDepartment(Collections.emptyList())
                .getAllBalance()
        );
    }

    @Test
    public void testGetAllBalanceForOneATM() {
        ATM atm = mock(ATM.class);
        when(atm.getBalance())
                .thenReturn(Commons.ofMap(Pair.of(Currency.EURO, 10_000L),
                        Pair.of(Currency.RUB, 100_000L)
                ));
        ATMDepartment atmDepartment = new NamedATMDepartment(Collections.singletonList(atm));
        Assert.assertEquals("Wrong summary of atms balance in department",
                Commons.ofMap(Pair.of(Currency.EURO, 10_000L), Pair.of(Currency.RUB, 100_000L)),
                atmDepartment.getAllBalance()
        );
    }

    @Test
    public void testGetAllBalanceForSomeATMs(){
        List<ATM> atmList = new ArrayList<>();

        ATM atm = mock(ATM.class);
        when(atm.getBalance())
                .thenReturn(Commons.ofMap(Pair.of(Currency.EURO, 10_000L),
                        Pair.of(Currency.RUB, 100_000L)
                ));
        atmList.add(atm);

        atm = mock(ATM.class);
        when(atm.getBalance())
                .thenReturn(Commons.ofMap(Pair.of(Currency.RUB, 100_010L)
                ));
        atmList.add(atm);

        atm = mock(ATM.class);
        when(atm.getBalance())
                .thenReturn(Commons.ofMap(Pair.of(Currency.EURO, 100L)
                ));
        atmList.add(atm);

        ATMDepartment atmDepartment = new NamedATMDepartment(atmList);
        Assert.assertEquals("Wrong summary of atms balance in department",
                Commons.ofMap(Pair.of(Currency.EURO, 10_100L), Pair.of(Currency.RUB, 200_010L)),
                atmDepartment.getAllBalance()
        );
    }

    @Test
    public void testRestoreAllToDefaultState() {
        ATM atm = mock(ATM.class);
        ATMDepartment atmDepartment = new NamedATMDepartment(Collections.singleton(atm));
        atmDepartment.restoreAllToDefaultState();
        verify(atm).restoreToDefaultState();
    }

    @Test(expected = UndoNamedATMException.class)
    public void testRestoreAllToDefaultStateNegative1() {
        ATM atm = mock(ATM.class);
        ATMDepartment atmDepartment = new NamedATMDepartment(Collections.singleton(atm));
        doThrow(UndoNamedATMException.class).when(atm).restoreToDefaultState();
        atmDepartment.restoreAllToDefaultState();
    }

    @Test(expected = UndoNamedATMException.class)
    public void testRestoreAllToDefaultStateNegative2() {
        ATM atm1 = mock(ATM.class);
        ATM atm2 = mock(ATM.class);
        ATM atm3 = mock(ATM.class);

        ATMDepartment atmDepartment = new NamedATMDepartment(Arrays.asList(atm1, atm2, atm3));
        doThrow(UndoNamedATMException.class).when(atm1).restoreToDefaultState();

        try {
            atmDepartment.restoreAllToDefaultState();
        } catch (Exception e) {
            verify(atm1).restoreToDefaultState();
            verify(atm2).restoreToDefaultState();
            verify(atm3).restoreToDefaultState();
            Assert.assertEquals(e.getClass(), UndoNamedATMException.class);
            throw e;
        }
    }
}