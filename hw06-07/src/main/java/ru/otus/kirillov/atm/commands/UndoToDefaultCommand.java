package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.UndoToDefaultQuery;

/** Откат до начального состояния банкомата
 * @see ATM#restoreToDefaultState()
 * Created by Александр on 07.12.2017.
 */
public class UndoToDefaultCommand extends AbstractCommand<UndoToDefaultQuery, Void> {

    public UndoToDefaultCommand(CellManagement cellManagement) {
        super(cellManagement);
    }

    @Override
    public Void execute(UndoToDefaultQuery query) {
        getCellManagement().restoreToInitial();
        return null;
    }
}
