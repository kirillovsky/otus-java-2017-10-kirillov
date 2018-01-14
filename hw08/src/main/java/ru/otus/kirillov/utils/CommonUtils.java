package ru.otus.kirillov.utils;

import java.util.Arrays;

/** Утилиты обзего назначения
 * Created by Александр on 14.01.2018.
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static void requiredNotNull(Object o) {
        if(o == null) {
            throw new IllegalArgumentException("Required must be not null!");
        }
    }

    public static void requiredNotNull(Object... o) {
        Arrays.stream(o).forEach(CommonUtils::requiredNotNull);
    }
}
