package ru.otus.kirillov.atm.commands;

import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.queries.Query;

/**
 * Интрерфейс выполнения операций над банкоматом
 * @param <R> - тип результата
 * @param <T> - тип входного запроса
 */
public interface Command<T extends Query, R> {

    R execute(T query);

    CellManagement getCellManagement();

    void setCellManagement(CellManagement cellManagement);
}
