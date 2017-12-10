package ru.otus.kirillov.atm.cells;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.RUBBanknote;

import static org.mockito.Mockito.*;

/** Тест системы управления банковскими ячейками
 * Created by Александр on 11.12.2017.
 */
public class CellRackTest {

    private CellRack cellManagement;

    @Before
    public void initialize() {
        cellManagement = new CellRack();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAbsentTargetCell() throws Exception {
        cellManagement.addCell(createCell(RUBBanknote.TEN_RUBLES));
        cellManagement.put(RUBBanknote.FIVE_HUNDRED_RUBLES, 10);
    }

    @Test
    public void testPutWithOneCell() throws Exception {
        Cell cell10 = createCell(RUBBanknote.TEN_RUBLES);
        Cell cell50 = createCell(RUBBanknote.FIFTY_RUBLES);
        cellManagement.addCells(cell10, cell50);

        cellManagement.put(RUBBanknote.FIFTY_RUBLES, 10);
        verify(cell50, times(1)).put(eq(10));
        verify(cell10, never());
    }

    @Test
    public void testPutWithSomeCells() throws Exception {
        Cell cell1 = createCell(RUBBanknote.TEN_RUBLES);
        Cell cell2 = createCell(RUBBanknote.TEN_RUBLES, 0);
        cellManagement.addCells(cell1, cell2);

        cellManagement.put(RUBBanknote.TEN_RUBLES, 10);
        verify(cell2, times(1)).put(eq(10));
        verify(cell1, never()).put(anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAbsentTargetCell() throws Exception {
        cellManagement.addCell(createCell(RUBBanknote.TEN_RUBLES));
        cellManagement.get(RUBBanknote.FIVE_HUNDRED_RUBLES, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNotEnoughBanknotes() throws Exception {
        Cell cell1 = createCell(RUBBanknote.ONE_HUNDRED_RUBLES, 2);
        cellManagement.addCell(cell1);
        cellManagement.get(RUBBanknote.ONE_HUNDRED_RUBLES, 10);

        verify(cell1, atLeastOnce()).getBanknoteCount();
        verify(cell1, atLeastOnce()).getBanknoteType();
        verify(cell1, never()).get(anyInt());
        verify(cell1, never()).put(anyInt());
    }

    @Test
    public void testGetFromOneCell() throws Exception {
        Cell cell10 = createCell(RUBBanknote.TEN_RUBLES);
        Cell cell50 = createCell(RUBBanknote.FIFTY_RUBLES);
        cellManagement.addCells(cell10, cell50);

        Assert.assertEquals(cellManagement.get(RUBBanknote.TEN_RUBLES, 2), 2);
        verify(cell10, times(1)).get(eq(2));
        verify(cell50, never()).get(anyInt());
    }

    @Test
    public void testGetFromSomeCells() throws Exception {
        Cell cell1 = createCell(RUBBanknote.TEN_RUBLES);
        Cell cell2 = createCell(RUBBanknote.TEN_RUBLES, 10);
        Cell cell3 = createCell(RUBBanknote.TEN_RUBLES, 3);
        Cell cell50 = createCell(RUBBanknote.FIFTY_RUBLES);
        cellManagement.addCells(cell1, cell2, cell3, cell50);

        Assert.assertEquals(cellManagement.get(RUBBanknote.TEN_RUBLES, 111), 111);
        verify(cell1, times(1)).get(eq(100));
        verify(cell2, times(1)).get(eq(10));
        verify(cell3, times(1)).get(eq(1));
        verify(cell50, never()).get(anyInt());
    }

    @Test
    public void testRestoreToInitial() throws Exception {
        Cell cell1 = createCell(RUBBanknote.TEN_RUBLES);
        cellManagement.addCells(cell1);

        cellManagement.restoreToInitial();
        verify(cell1, only()).restoreToInitial();
    }

    private Cell createCell(Banknote banknote, int count) {
        Cell result = mock(Cell.class);
        when(result.getBanknoteType()).thenReturn(banknote);
        when(result.getBanknoteCount()).thenReturn(count);
        return result;
    }

    private Cell createCell(Banknote banknote) {
        return createCell(banknote, 100);
    }
}