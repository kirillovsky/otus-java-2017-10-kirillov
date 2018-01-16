package ru.otus.kirillov.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Рефлекшн утилз.
 * Когда в проект добавляется еще один класс утилит
 * в мире грустит один ярый преверженец ООП :)
 * Created by Александр on 12.01.2018.
 */
public final class ReflectionUtils {

    private static final int SYNTHETIC = 0x00001000;

    private ReflectionUtils() {
    }

    public static List<Field> getSerializedFields(Class<?> clazz) {
        CommonUtils.requiredNotNull(clazz);
        return getAllFields(clazz).stream()
                .filter(ReflectionUtils::isSerialized)
                .collect(Collectors.toList());
    }

    public static Map<String, Object> getSerializedFieldNameValueMap(Object o) {
        CommonUtils.requiredNotNull(o);
        Class<?> clazz = o.getClass();
        Map<String, Object> result = new HashMap<>();
        getSerializedFields(clazz).forEach(
                f -> result.put(f.getName(), ReflectionUtils.getFieldValue(f, o))
        );
        return result;
    }

    public static boolean isLocalClass(Class<?> clazz) {
        return clazz.isLocalClass();
    }

    public static boolean isNonStaticMember(Class<?> clazz) {
        return clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

    /**
     * Основная логика для проверки допустимости сериализации поля
     * @param field
     * @return
     */
    public static boolean isSerialized(Field field) {
        int fieldModifiers = field.getModifiers();
        return !(Modifier.isNative(fieldModifiers)
                || Modifier.isStatic(fieldModifiers)
                || Modifier.isTransient(fieldModifiers)
                || isSynthetic(fieldModifiers)
        ) && !isLocalClass(field.getType()) && !isNonStaticMember(field.getType());
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        CommonUtils.requiredNotNull(clazz);
        List<Field> fields = new ArrayList<>();
        for (Class<?> tmp = clazz; tmp != null; tmp = tmp.getSuperclass()) {
            fields.addAll(Arrays.asList(tmp.getDeclaredFields()));
        }
        return fields;
    }

    private static boolean isSynthetic(int modifier) {
        return (modifier & SYNTHETIC) != 0;
    }

    private static Object getFieldValue(Field fld, Object obj) {
        boolean oldAccessible = fld.isAccessible();
        if (!oldAccessible) {
            fld.setAccessible(true);
        }

        try {
            return fld.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("IllegalAccessException for class %s field %s", obj.getClass(), fld), e
            );
        } finally {
            fld.setAccessible(oldAccessible);
        }
    }

}
