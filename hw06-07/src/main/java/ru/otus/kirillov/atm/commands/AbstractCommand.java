package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.Query;
import ru.otus.kirillov.atm.utils.Commons;

/** Абстрактная команда. Так как было решено передавать CellManagement не через
 * запрос. А устанавливать его заранее. В частности, это позволило уменьшить
 * коуплинг запросов.
 * Created by Александр on 09.12.2017.
 */
public abstract class AbstractCommand <T extends Query, R> implements Command<T, R> {

    private CellManagement cellManagement;

    public AbstractCommand(CellManagement cellManagement) {
        this.cellManagement = cellManagement;
    }

    @Override
    public CellManagement getCellManagement() {
        return cellManagement;
    }

    @Override
    public void setCellManagement(CellManagement cellManagement) {
        Commons.requiredNotNull(cellManagement, "Cell management must be not null");
        this.cellManagement = cellManagement;
    }
}
