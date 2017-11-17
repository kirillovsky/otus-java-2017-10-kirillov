package ru.otus.kirillov.hw05.mytest.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

/** Набор утилит общего характера
 * Created by Александр on 15.11.2017.
 */
public final class CommonUtils {

    private CommonUtils() {}

    public static <T> void requiredPredicate(Predicate<? super T> tst, T obj, String msg) {
        if(!tst.test(obj)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void requiredNotEmptyString(String str, String msg) {
        requiredPredicate(StringUtils::isNotEmpty, str, msg);
    }

    public static void requiredNotNull(Object obj, String msg) {
        requiredPredicate(o -> o != null, obj, msg);
    }
}
