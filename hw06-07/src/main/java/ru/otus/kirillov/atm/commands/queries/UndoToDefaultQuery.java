package ru.otus.kirillov.atm.commands.queries;

/** Запрос на откат по текущей операции
 * Created by Александр on 07.12.2017.
 */
public class UndoToDefaultQuery extends Query{

    public UndoToDefaultQuery() {
        super(Type.UNDO_TO_INITIAL);
    }
}
