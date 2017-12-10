package ru.otus.kirillov.atm.cells;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.utils.Commons;

/** Ячейка банкомата для купюр
 * Created by Александр on 03.12.2017.
 */
public class Cell {

    /**
     * Сохранение состояние ячейки при создании
     */
    public class CellMemento {
        private final int banknoteCout;

        public CellMemento() {
            banknoteCout = Cell.this.banknoteCount;
        }
    }

    private final CellMemento INITIAL_STATE;

    private final Banknote banknoteType;

    private int banknoteCount;

    public Cell(Banknote banknoteType, int banknoteCount) {
        this.banknoteType = banknoteType;
        this.banknoteCount = banknoteCount;
        INITIAL_STATE = new CellMemento();
    }

    public Banknote getBanknoteType() {
        return banknoteType;
    }

    public int getBanknoteCount() {
        return banknoteCount;
    }

    public boolean isNotEmpty() {
        return getBanknoteCount() != 0;
    }

    /**
     * Внесение купюр в ячейку
     * @param count
     */
    public void put(int count) {
        Commons.requiredTrue(count > 0, "Banknote count must be more than zero");
        banknoteCount += count;
    }

    /**
     * Выдача купюр из ячейки
     * @param banknoteCount
     * @return
     */
    public Pair<Banknote, Integer> get(int banknoteCount) {
        Commons.requiredMoreThanZero(banknoteCount,
                "banknote count must me more than zero");
        Commons.requiredTrue(this.banknoteCount - banknoteCount > 0,
                "banknote count must not be negative after withdrawal");

        this.banknoteCount -= banknoteCount;
        return Pair.of(banknoteType, banknoteCount);
    }


    /**
     * Сохранение текущего состояния ячейки
     * @return снимок состояния
     */
    public CellMemento save() {
        return new CellMemento();
    }


    /**
     * Восстановитиь состояние ячейки из снимка
     * @param memento - снимок состояния
     */
    public void restore(CellMemento memento) {
        this.banknoteCount = memento.banknoteCout;
    }

    /**
     * Восстановить исходное состояние ячейки
     */
    public void restoreToInitial() {
        restore(INITIAL_STATE);
    }

}
