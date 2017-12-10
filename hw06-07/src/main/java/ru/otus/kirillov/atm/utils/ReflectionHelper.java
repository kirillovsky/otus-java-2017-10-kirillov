package ru.otus.kirillov.atm.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Урезанный набор утилит для работы с рефлексией
 * от Виталия Чибрикова. Взят из ДЗ №5
 * Created by tully.
 */
public class ReflectionHelper {

    private ReflectionHelper() {
    }

    public static Object callMethod(Object object, String name, Object... args) {
        Class<?>[] argsClasses = toClasses(args);
        return callMethod(object, getMethodByName(object.getClass(), name, argsClasses),
                args);
    }

    public static Object callMethod(Object obj, Method method, Object... args) {
        Commons.requiredNotNull(method, "Method must be not null");
        boolean isAccessible = true;
        try {
            isAccessible = method.isAccessible();
            if (!isAccessible) {
                method.setAccessible(true);
            }
            return method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } finally {
            if (method != null && !isAccessible) {
                method.setAccessible(false);
            }
        }
    }

    public static Method getMethodByName(Class<?> clazz, String methodName, Class<?>... argsType) {
        Commons.requiredNotNull(clazz, "Class must be not null");
        Commons.requiredNotNull(methodName, "Method name mus be not null");
        try {
            return clazz.getDeclaredMethod(methodName, argsType);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return (Class<?>[]) Arrays.stream(args)
                .map(Object::getClass)
                .toArray();
    }
}
