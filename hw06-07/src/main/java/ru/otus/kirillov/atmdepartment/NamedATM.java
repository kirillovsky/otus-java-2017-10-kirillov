package ru.otus.kirillov.atmdepartment;

import ru.otus.kirillov.atm.ATM;

/** Именованный ATM.
 * Created by Александр on 09.12.2017.
 */
public interface NamedATM extends ATM {

    /**
     * Идентификатор ATM в департаменте.
     * @return
     */
    String getName();
}
