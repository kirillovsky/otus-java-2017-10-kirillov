package ru.otus.kirillov.atmdepartment;

import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.utils.Commons;
import ru.otus.kirillov.atmdepartment.exception.UndoNamedATMException;

import java.util.*;

/**
 * Департамент банкоматов. Реализация
 *
 * @see ATMDepartment
 * Created by Александр on 09.12.2017.
 */
public class NamedATMDepartment implements ATMDepartment {

    private List<NamedATM> atms = new ArrayList<>();

    public NamedATMDepartment() {
    }

    public NamedATMDepartment(Collection<ATM> atms) {
        Commons.requiredNotNull(atms, "atms required must be not null");
        atms.forEach(this::addATM);
    }

    @Override
    public Map<Currency, Long> getAllBalance() {
        return atms.stream()
                .map(ATM::getBalance)
                .reduce(new HashMap<>(),
                        (a, b) -> {
                            Map<Currency, Long> accumulator = new HashMap<>(a);
                            b.forEach((k, v) -> accumulator.merge(k, v, Long::sum));
                            return accumulator;
                        }
                );
    }

    @Override
    public void restoreAllToDefaultState() {
        List<String> failedATMRestore = new ArrayList<>();

        atms.forEach(
                (atm) -> {
                    try {
                        atm.restoreToDefaultState();
                    } catch (Exception e) {
                        failedATMRestore.add(atm.getName());
                    }
                }
        );

        if (!failedATMRestore.isEmpty()) {
            throw new UndoNamedATMException(failedATMRestore);
        }
    }

    @Override
    public void addATM(ATM atm) {
        addATM(NamedATMSimpleFactory.createNamedATM(atm));
    }

    public void addATM(NamedATM atm) {
        Commons.requiredNotNull(atm, "atm required not null");
        atms.add(atm);
    }

    @Override
    public void addATMs(Collection<ATM> atms) {
        Commons.requiredNotNull(atms, "atms required not null");
        atms.forEach(this::addATM);
    }
}
