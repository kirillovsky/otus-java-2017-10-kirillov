package ru.otus.kirillov.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Утилиты обзего назначения
 * Created by Александр on 14.01.2018.
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static void requiredNotNull(Object o) {
        requiredNotNull(o, "Required must be not null!");
    }

    public static void requiredNotNull(Object o, String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> T retunIfNotNull(T value) {
        requiredNotNull(value);
        return value;
    }

    public static <T> T retunIfNotNull(T value, String msg) {
        requiredNotNull(value, msg);
        return value;
    }

    public static void requiredNotNull(Object... o) {
        Arrays.stream(o).forEach(CommonUtils::requiredNotNull);
    }

    /**
     * Получение имени класса (некоего, но единообразно)
     *
     * @param clazz
     * @return
     */
    public static String getClassName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return clazz.getSimpleName();
    }

    /**
     * Получение имени метода, в котором происходит текущее выполнение
     *
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

    public static <T> Function<T, Object> withoutResult(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return null;
        };
    }

    public static <K, V> void putPair(Map<K, V> map, Pair<K, V> pair) {
        CommonUtils.requiredNotNull(map, pair);
        map.put(pair.getKey(), pair.getValue());
    }
}
