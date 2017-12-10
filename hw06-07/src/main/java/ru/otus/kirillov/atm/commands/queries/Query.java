package ru.otus.kirillov.atm.commands.queries;

import ru.otus.kirillov.atm.utils.Commons;

/**
 * Created by Александр on 05.12.2017.
 */
public abstract class Query {

    private Type type;

    public enum Type {
        /**
         * Снятие наличных
         */
        WITHRDAWAL,

        /**
         * Внесение наличных
         */
        DEPOSITING,

        /**
         * Откат до дефолтного состояния
         */
        UNDO_TO_INITIAL,

        /**
         * Сумма остатка денежных средств по каждой валюте
         */
        BALANCE
    }

    protected Query(Type type) {
        Commons.requiredNotNull(type, "type must be not null");
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
