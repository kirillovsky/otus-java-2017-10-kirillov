package ru.otus.kirillov.atm.commands;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.UndoToDefaultQuery;
import ru.otus.kirillov.atm.money.Banknote;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/** Команда отката внутреннего состояния банкомата до дефолтного состояния
 * Created by Александр on 10.12.2017.
 */
public class UndoToDefaultCommandTest {

    private CellManagement cellManagement;
    private Command<UndoToDefaultQuery, Void> command;

    @Before
    public void initialize() {
        cellManagement = mock(CellManagement.class);
        command = new UndoToDefaultCommand(cellManagement);
    }

    @Test
    public void testExecute() throws Exception {
        command.execute(new UndoToDefaultQuery());
        verify(cellManagement, atLeastOnce()).restoreToInitial();
        verify(cellManagement, never()).get(any(Banknote.class), anyInt());
        verify(cellManagement, never()).put(any(Banknote.class), anyInt());
        verify(cellManagement, never()).getBanknoteCountByType();
    }
}