package ru.otus.kirillov.atm.cells;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.money.RUBBanknote;

import static org.junit.Assert.*;

/**
 * Created by Александр on 11.12.2017.
 */
public class CellTest {

    private static int DEFAULT_SIZE = 100;
    private Cell cell;

    @Before
    public void defaultInitialize() {
        cell = new Cell(RUBBanknote.FIFTY_RUBLES, DEFAULT_SIZE);
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        Assert.assertTrue(cell.isNotEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutLessThanZero() throws Exception {
        cell.put(0);
    }

    @Test
    public void testPut() {
        cell.put(102);
        Assert.assertEquals(cell.getBanknoteCount(), DEFAULT_SIZE + 102);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLessThanZero() throws Exception {
        cell.get(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMoreThanContains() throws Exception {
        cell.get(DEFAULT_SIZE + 1);
    }

    @Test
    public void testGet() {
        cell.get(10);
        Assert.assertEquals(cell.getBanknoteCount(), DEFAULT_SIZE - 10);
    }

    @Test
    public void testGetAll() {
        cell.get(DEFAULT_SIZE);
        Assert.assertEquals(cell.getBanknoteCount(), 0);
    }

    @Test
    public void testSave() throws Exception {
        cell.put(100);
        Cell.CellMemento memento = cell.save();
        Assert.assertEquals(memento.banknoteCount, DEFAULT_SIZE + 100);
    }

    @Test
    public void testRestore() throws Exception {
        cell.put(100);
        Cell.CellMemento memento = cell.save();
        cell.get(DEFAULT_SIZE + 100);
        cell.restore(memento);
        Assert.assertEquals(cell.getBanknoteCount(), DEFAULT_SIZE + 100);
    }

    @Test
    public void testRestoreToInitial() throws Exception {
        cell.get(10);
        Assert.assertEquals(cell.getBanknoteCount(), DEFAULT_SIZE - 10);
        cell.restoreToInitial();
        Assert.assertEquals(cell.getBanknoteCount(), DEFAULT_SIZE);
    }
}