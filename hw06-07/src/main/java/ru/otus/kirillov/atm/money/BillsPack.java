package ru.otus.kirillov.atm.money;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Пачка денег.
 * Created by Александр on 03.12.2017.
 */
public final class BillsPack {

    /**
     * Множество пар (тип банкноты, количество купюр)
     */
    private Map<Banknote, Integer> banknotesPack = new HashMap<>();

    /**
     * Добавить банкноты в пачку
     * @param banknote - тип банкнот
     * @param count - кол-во банкнот
     */
    public void addBanknotes(Banknote banknote, int count) {
        Commons.requiredMoreThanZero(count, "banknote count must be more than zero");
        banknotesPack.merge(banknote, count, Integer::sum);
    }

    /**
     * Вернуть содержимое пачки купюр по типам банкнот
     * @return
     */
    public List<Pair<Banknote, Integer>> getCountsByType() {
        return banknotesPack.entrySet().stream()
                .collect(Collectors.mapping(
                        entry -> Pair.of(entry.getKey(), entry.getValue()),
                        Collectors.toList()
                ));
    }

    /**
     * Вернуть типы банкнот в пачке
     * @return
     */
    public Set<Banknote> getBankonotesType() {
        return banknotesPack.keySet();
    }
}
