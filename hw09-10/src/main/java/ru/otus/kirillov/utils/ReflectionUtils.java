package ru.otus.kirillov.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.DataSet;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
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
    private static final String DEFAULT_REF_FIELD_POSTFIX = "_id";

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

    public static <T> Class<T> getSuperClassGenericArgument(Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) CommonUtils.retunIfNotNull(clazz)
                .getGenericSuperclass()).getActualTypeArguments()[0];
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
     * в него параметр {@param obj}. Здесь возможно 2 случая:
     * 1. У него есть конструктор с одним параметром {@param <I>}
     * 2. У него есть дефолтный конструктор и поле {@param <I>}
     *
     * @param type - класс инстанцируемого объекта
     * @param obj  - инжектируемый объект
     * @param <T>  - тип инстанцируемого объекта
     * @param <I>  - тип инжектируемого объекта
     * @return инстанс
     */
    public static <T, I> T instantiateWithInjections(Class<?> type, I obj) {
        return instantiateWithInjections(type, new Object[]{obj});
    }

    /**
     * Попытка создать инстанс объекта класса {@param type} и проинджектить
     * в него параметры {@param objects}. Здесь возможно 2 случая:
     * 1. У него есть конструктор с параметрами (в переданном порядке)
     * 2. У него есть дефолтный конструктор и поле - для случая полей разных типов (иначе см. первый случай*
     * @param type    - класс инстанцируемого объекта
     * @param objects - инжектируемые объекты
     * @param <T>     - тип инстанцируемого объекта
     * @return инстанс
     */
    public static <T> T instantiateWithInjections(Class<?> type, Object... objects) {
        T instance = instantiate(type, objects);
        if (instance == null && hasConstructor(type, (Class<?>[]) Arrays.stream(objects)
                .map(Object::getClass)
                .collect(Collectors.toList())
                .toArray(new Class<?>[]{}))) {
            instance = instantiate(type, objects);
        }
        if (instance == null && hasConstructor(type)) {
            instance = instantiate(type);
            for (Object obj : objects) {
                if (!hasSingleFieldByType(type, obj.getClass())) {
                    return null;
                }
                instance = withFieldValue(instance, getField(type, obj.getClass()), obj);
            }
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

    public static Class<?> getParentGenericType(Class<?> clazz) {
        CommonUtils.requiredNotNull(clazz);
        Type result = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        return result instanceof Class ? (Class) result : null;
    }

    public static Class<?> getGenericType(Field field) {
        CommonUtils.requiredNotNull(field);
        Type result = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
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

    public static boolean isAnnotated(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        CommonUtils.requiredNotNull(annotationClass, "Annotation clazz must be not null");
        return clazz.isAnnotationPresent(annotationClass);
    }

    public static boolean isAnnotated(Field field, Class<? extends Annotation> annotationClass) {
        CommonUtils.requiredNotNull(field, "Field must be not null");
        CommonUtils.requiredNotNull(annotationClass, "Annotation clazz must be not null");
        return field.isAnnotationPresent(annotationClass);
    }

    public static boolean hasAnnotations(Field field) {
        CommonUtils.requiredNotNull(field, "Field must be not null");
        return field.getAnnotations().length != 0;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<? extends T> annotationClass) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        CommonUtils.requiredNotNull(annotationClass, "Annotation clazz must be not null");
        return clazz.getAnnotation(annotationClass);
    }

    public static <T extends Annotation> T getAnnotation(Field field, Class<? extends T> annotationClass) {
        CommonUtils.requiredNotNull(field, "Class must be not null");
        CommonUtils.requiredNotNull(annotationClass, "Annotation clazz must be not null");
        return field.getAnnotation(annotationClass);
    }

    public static String getTableName(Class<? extends DataSet> clazz) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        if (!isAnnotated(clazz, Table.class)) {
            return clazz.getSimpleName().toLowerCase();
        }
        String resultName = getAnnotation(clazz, Table.class).name();
        return resultName.isEmpty() ? clazz.getSimpleName().toLowerCase() : resultName;
    }

    public static String getSqlColumnName(Field field) {
        CommonUtils.requiredNotNull(field, "field must be not null");
        if (!isAnnotated(field, Column.class)) {
            return field.getName().toLowerCase();
        }
        String resultName = getAnnotation(field, Column.class).name();
        return resultName.isEmpty() ? field.getName().toLowerCase() : resultName;
    }

    public static String getRefFieldName(Field field, Class<? extends Annotation> annotationClass, String defaultPrefix) {
        CommonUtils.requiredNotNull(field, "field must be not null");
        CommonUtils.requiredNotNull(annotationClass, "annotationClass must be not null");

        if (!isAnnotated(field, annotationClass)) {
            throw new RuntimeException("This is not OneToOne field");
        }

        if (isAnnotated(field, JoinColumn.class)) {
            return getAnnotation(field, JoinColumn.class).name();
        }

        return CommonUtils.retunIfNotNull(defaultPrefix) + DEFAULT_REF_FIELD_POSTFIX;
    }


}
