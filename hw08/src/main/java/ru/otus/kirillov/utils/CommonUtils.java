package ru.otus.kirillov.utils;

import java.util.Arrays;
import java.util.Objects;

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

    public static <T> T retunIfNotNull(T value) {
        requiredNotNull(value);
        return value;
    }

    public static void requiredNotNull(Object... o) {
        Arrays.stream(o).forEach(CommonUtils::requiredNotNull);
    }

    /**
     * Получение имени класса (некоего, но единообразно)
     * @param clazz
     * @return
     */
    public static String getClassName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getSimpleName();
    }

    /**
     * Получение имени метода, в котором происходит текущее выполнение
     * @return имя метода, в котором {@link CommonUtils#getMethodName()}
     * был вызван.
     */
    public static String getMethodName() {
        return getStackElement(1).getMethodName();
    }

    public static String getClassName() {
        return getStackElement(1).getClassName();
    }

    private static StackTraceElement getStackElement(int i) {
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = th.getStackTrace();
        // i + 1, где 1 - значит, что стектрейс элемент для метода getStackElement(int)
        // лежащий под индексом 0 нас не интересует
        return stackTrace[i + 1];
    }
}
