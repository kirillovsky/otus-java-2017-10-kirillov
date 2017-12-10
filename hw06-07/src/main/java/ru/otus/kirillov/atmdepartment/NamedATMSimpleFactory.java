package ru.otus.kirillov.atmdepartment;

import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.Map;
import java.util.UUID;

/**
 * Простая фабрика создания {@link NamedATM}
 * из {@link ATM}
 * Created by Александр on 09.12.2017.
 */
public class NamedATMSimpleFactory {

    private static class NamedATMDecorator implements NamedATM {

        private static final String ATM_NAME_PREFIX = "ATM_";
        private ATM atm;
        private String name;

        NamedATMDecorator(ATM atm) {
            this.atm = atm;
            this.name = ATM_NAME_PREFIX + UUID.randomUUID().toString();
        }

        NamedATMDecorator(NamedATM atm) {
            this.atm = atm;
            this.name = atm.getName();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void restoreToDefaultState() {
            atm.restoreToDefaultState();
        }

        @Override
        public void deposit(BillsPack billsPack) {
            atm.deposit(billsPack);
        }

        @Override
        public Map<Currency, Long> getBalance() {
            return atm.getBalance();
        }

        @Override
        public BillsPack withdraw(Currency currency, long sum) {
            return atm.withdraw(currency, sum);
        }

        @Override
        public String toString() {
            return "ATM[ " + name + "]";
        }
    }

    /**
     * Создание стандартного именованного ATM.
     *
     * @param atm - входной ATM
     * @return
     */
    public static NamedATM createNamedATM(ATM atm) {
        Commons.requiredNotNull(atm, "atm must be null");
        return new NamedATMDecorator(atm);
    }
}
