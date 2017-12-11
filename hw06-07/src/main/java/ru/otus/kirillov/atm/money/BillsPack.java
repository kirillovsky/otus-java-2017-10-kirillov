package ru.otus.kirillov.atm.money;

import ru.otus.kirillov.atm.utils.Commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Пачка денег.
 * Created by Александр on 03.12.2017.
 */
public class BillsPack {

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

    public BillsPack withNewBanknotes(Banknote banknote, int count) {
        addBanknotes(banknote, count);
        return this;
    }

    /**
     * Вернуть содержимое пачки купюр по типам банкнот
     * @return
     */
    public Map<Banknote, Integer> getCountsByType() {
        return new HashMap<>(banknotesPack);
    }

    /**
     * Вернуть типы банкнот в пачке
     * @return
     */
    public Set<Banknote> getBanknotesType() {
        return banknotesPack.keySet();
    }

    public static BillsPack of(Banknote banknote, int count) {
        BillsPack result = new BillsPack();
        result.addBanknotes(banknote, count);
        return result;
    }
}
