package ru.otus.kirillov.utils;

import java.util.Objects;

/**
 * Рефлекшн утилз.
 * Когда в проект добавляется еще один класс утилит
 * в мире грустит один ярый преверженец ООП :)
 * Created by Александр on 12.01.2018.
 */
public final class ReflectionUtils {

    private ReflectionUtils() {}

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
     * @return имя метода, в котором {@link ReflectionUtils#getMethodName()}
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
        return stackTrace[i];
    }
}
