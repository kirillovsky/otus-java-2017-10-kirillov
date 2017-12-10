package ru.otus.kirillov.atm.money;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Типы банкнот. Наследники должны быть enum!
 * Created by Александр on 03.12.2017.
 */
public interface Banknote  {

    /**
     * Тип валюты
     * @return
     */
    Currency getCurrency();

    /**
     * Получить номинал банкноты
     * @return
     */
    long getNominal();

    /**
     * Получить денежное значение банкнот
     * @param banknote
     * @param count
     * @return
     * @throws IllegalAccessError если count <= 0
     */
    static long getSum(Banknote banknote, int count) {
        Commons.requiredTrue(count >= 0, "Count must be positive");
        return banknote.getNominal() * count;
    }

    /**
     * Получить денежное значение банкнот
     * @param banknoteCountPair - пара (тип банкноты, количество)
     * @return
     */
    static long getSum(Pair<Banknote, Integer> banknoteCountPair) {
        return getSum(banknoteCountPair.getKey(), banknoteCountPair.getValue());
    }

    /**
     * Компаратор для сравнения банкнот по их номиналу
     * от большего к меньшему
     * @return компаратор
     */
    static Comparator<? super Banknote> comparatorByNominal() {
        return Comparator.comparing(Banknote::getNominal, Comparator.reverseOrder());
    }

    /**
     * @see Banknote#comparatorByNominal(),
     * но для пар (тип банкноты, количество)
     * @return
     */
    static Comparator<? super Pair<Banknote, Integer>> comparatorByNominalForPair() {
        return Comparator.comparing(Pair::getKey, comparatorByNominal());
    }

    /**
     * Функция определяет, сколькими купюрами типа {@param banknote}
     * можно набрать сумму максимально близку снизу к {@param sum}
     * @param banknote - тип банкоты
     * @param sum - сумма в валюте банкноты
     * @return
     */
    static int getBanknoteCountForSum(Banknote banknote, long sum) {
        return (int) (sum / banknote.getNominal());
    }
}
