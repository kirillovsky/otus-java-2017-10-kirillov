package ru.otus.kirillov.atm.utils;

import java.util.Objects;

/** Набор общих утилит
 * Created by Александр on 04.12.2017.
 */
public final class Commons {

    private Commons() {}

    public static <T> void requiredEquals(T o1, T o2, String message) {
        requiredNotNull(o1, "First arg must be not null");
        requiredNotNull(o2, "Second arg must be not null");
        requiredTrue(o1.equals(o2), message);
    }

    public static void requiredTrue(boolean test, String message) {
        if(!test) {
            throw new IllegalStateException(message);
        }
    }

    public static void requiredNotNull(Object o, String message) {
        Objects.requireNonNull(o, message);
    }

    public static void requiredMoreThanZero(long value, String message) {
        requiredTrue(value > 0, message);
    }
}
