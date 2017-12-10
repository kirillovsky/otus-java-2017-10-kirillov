package ru.otus.kirillov.atm.cells;

import ru.otus.kirillov.atm.money.Banknote;

import java.util.Map;

/** Интерфейс управления ячейками банкомата
 * Created by Александр on 06.12.2017.
 */
public interface CellManagement {

    /**
     * Положить купюры в ячейку. Если ячеек с данным типом банкнот
     * несколько, то выбирается та, в которой меньше всего купюр.
     * @param banknote - тип банкоты
     * @param count - тип ячейки
     * @throws IllegalArgumentException - если отсутствует соответствующая ячейка
     * для типа {@param banknote}, или {@param count} <= 0
     */
    void put(Banknote banknote, int count);

    /**
     * Выдача банкнот. По умолчанию система пытается забрать
     * банкноты из ячейки с максимальным их кол-вом. Если
     * банкнот в такой ячейке не хватает, но есть ячейки
     * с таким же типом банкнот, то система попытается забрать
     * все банкноты из первой ячейки и далее будет забирать из следующих
     * с тем же типом банкнот.
     * @param banknote - тип банкноты
     * @param count - кол-во необходимых банкнот
     * @return кол-во извлеченных банкнот
     */
    int get(Banknote banknote, int count);

    /**
     * Получить кол-во банкнот по их типам,
     * находящихся в ячейках стойки.
     * @return ассоциативный массив типов купюр и их количеств
     */
    Map<Banknote, Integer> getBanknoteCountByType();

    /**
     * Восстановить состояние ячеек стойки до первоначального
     */
    void restoreToInitial();


}
