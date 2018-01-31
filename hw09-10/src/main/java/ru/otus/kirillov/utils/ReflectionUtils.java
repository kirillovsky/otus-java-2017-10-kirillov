package ru.otus.kirillov.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Рефлекшн утилз.
 * Когда в проект добавляется еще один класс утилит
 * в мире грустит один ярый преверженец ООП :)
 * Created by Александр on 12.01.2018.
 */
// TODO: 23.01.2018 Зачистить от ненужных методов
public final class ReflectionUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    private ReflectionUtils() {
    }

    public static Stream<Field> getAllFields(Class<?> clazz) {
        CommonUtils.requiredNotNull(clazz);
        List<Field> fields = new ArrayList<>();
        for (Class<?> tmp = clazz; tmp != null; tmp = tmp.getSuperclass()) {
            fields.addAll(Arrays.asList(tmp.getDeclaredFields()));
        }
        return fields.stream();
    }

    /**
     * Инстанцирование нового объекта класса, c заданными аргументами
     *
     * @param type - класс инстанцируемого объекта
     * @param args - аргументы, необходимые для его создания
     * @return объект нового класса, или null, если его создать не удалось
     */
    public static <T> T instantiate(Class<?> type, Object... args) {
        try {
            if (args.length == 0) {
                return (T) type.newInstance();
            } else {
                return (T) type.getDeclaredConstructor(toClasses(args)).newInstance(args);
            }
        } catch (ReflectiveOperationException e) {
            LOGGER.debug(e);
        }
        return null;
    }

    /**
     * Попытка создать инстанс объекта класса {@param type} и проинджектить
     * в него параметр {@param obj}. Здесь возможно 3 случая:
     * 1. У него есть конструктор с одним параметров {@param <I>}
     * 2. У него есть дефолтный конструктор и поле {@param <I>}
     * 3. Он должен иметь модификатор public
     *
     * @param type - класс инстанцируемого объекта
     * @param obj  - инжектируемый объект
     * @param <T>  - тип инстанцируемого объекта
     * @param <I>  - тип инжектируемого объекта
     * @return инстанс
     */
    public static <T, I> T instantiateWithInjections(Class<?> type, I obj) {
        T instance = instantiate(type, obj);
        if (instance == null && hasConstructor(type, obj.getClass())) {
            instance = instantiate(type, obj);
        }
        if (instance == null && hasConstructor(type)
                && hasSingleFieldByType(type, obj.getClass())) {
            instance = withFieldValue(instantiate(type),
                    getField(type, obj.getClass()), obj);
        }
        return instance;
    }

    public static <T> boolean hasConstructor(Class<T> type, Class<?>... args) {
        try {
            return type.getDeclaredConstructor(args) != null;
        } catch (NoSuchMethodException | SecurityException e) {
            LOGGER.debug(e);
            return false;
        }
    }

    public static boolean hasSingleFieldByType(Class<?> clazz, Class<?> fieldType) {
        return getField(clazz, fieldType) != null;
    }

    public static Field getField(Class<?> clazz, Class<?> fieldType) {
        return getAllFields(clazz)
                .filter(f -> f.getType().isAssignableFrom(fieldType))
                .findFirst().orElse(null);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return getFieldNonSafe(clazz, fieldName);
        } catch (NoSuchFieldException e) {
            LOGGER.debug(e);
            return null;
        }
    }

    protected static Field getFieldNonSafe(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return getAllFields(clazz)
                .filter(f -> f.getName().equals(fieldName))
                .findFirst().orElseThrow(NoSuchFieldException::new);
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = getFieldNonSafe(object.getClass(), fieldName);
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.debug(e);
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOGGER.debug(e);
            throw new RuntimeException(e);
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    public static void setFieldCollectionValue(Object object, Field field, Collection value) {
        if (field.getType() == List.class) {
            setFieldValue(object, field, new ArrayList<>(value));
        } else if (field.getType() == Set.class) {
            setFieldValue(object, field, new HashSet<>(value));
        } else {
            setFieldValue(object, field, value);
        }
    }

    public static <T> T withFieldValue(T object, Field field, Object value) {
        T result = object;
        try {
            setFieldValue(object, field, value);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static Class<?> getGenericType(Class<?> clazz) {
        Type result = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        return result instanceof Class ? (Class) result : null;
    }

    private static Class<?>[] toClasses(Object[] args) {
        List<Class<?>> classes = Arrays.stream(args)
                .map(Object::getClass)
                .collect(Collectors.toList());
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public static Object getFieldValue(Field fld, Object obj) {
        boolean oldAccessible = fld.isAccessible();
        if (!oldAccessible) {
            fld.setAccessible(true);
        }

        try {
            return fld.get(obj);
        } catch (IllegalAccessException e) {
            LOGGER.debug(e);
            throw new RuntimeException(
                    String.format("IllegalAccessException for class %s field %s", obj.getClass(), fld), e
            );
        } finally {
            fld.setAccessible(oldAccessible);
        }
    }


}
