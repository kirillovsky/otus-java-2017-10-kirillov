package ru.otus.kirillov.hw05.mytest.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import ru.otus.kirillov.hw05.mytest.annotations.After;
import ru.otus.kirillov.hw05.mytest.annotations.Before;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;
import ru.otus.kirillov.hw05.mytest.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tully.
 */
public class ReflectionHelper {

    //todo: добавить логгер

    interface ReflectionBiFunction<T, U, R> {
        R apply(T o, U o2) throws ReflectiveOperationException;
    }

    private ReflectionHelper() {
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.newInstance();
            } else {
                return type.getConstructor(toClasses(args)).newInstance(args);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getClass(String classFullName) {
        try {
            Class<?> clazz = Class.forName(classFullName);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static List<Method> getTestMethods(Class<?> clazz) {
        return getAnnotatedMethods(clazz, Test.class);
    }

    public static List<Method> getAfterMethods(Class<?> clazz) {
        return getAnnotatedMethods(clazz, After.class);
    }

    public static List<Method> getBeforeMethods(Class<?> clazz) {
        return getAnnotatedMethods(clazz, Before.class);
    }

    public static List<Class<?>> getTestClassesFromPackage(String packageFullName) {
        Reflections reflections = new Reflections(packageFullName);
        return new ArrayList<>(reflections.getTypesAnnotatedWith(TestCase.class));
    }

    public static boolean isTestCase(Class<?> clazz) {
        return isAnnotated(clazz, TestCase.class);
    }

    public static boolean isTest(Method method) {
        return isAnnotated(method, Test.class);
    }

    public static boolean hasDefaultPublicConstructor(Class<?> clazz) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getGenericParameterTypes().length == 0)
                .filter(c -> Modifier.isPublic(c.getModifiers()))
                .findAny()
                .isPresent();
    }

    public static boolean isAnnotated(Class<?> clazz, Class<? extends Annotation> annotation) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        CommonUtils.requiredNotNull(annotation, "Annotation clazz must be not null");
        return clazz.isAnnotationPresent(annotation);
    }

    public static boolean isAnnotated(Method method, Class<? extends Annotation> annotation) {
        CommonUtils.requiredNotNull(method, "Method must be not null");
        CommonUtils.requiredNotNull(annotation, "Annotation clazz must be not null");
        return ArrayUtils.contains(method.getDeclaredAnnotations(), annotation);
    }

    public static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        CommonUtils.requiredNotNull(annotationClazz, "Annotation clazz must be not null");
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter((method) -> method.isAnnotationPresent(annotationClazz))
                .collect(Collectors.toList());
    }

    static Object getFieldValue(Object object, String name) {
        return fieldValueOperation(object, name, Field::get);
    }

    static void setFieldValue(Object object, String name, Object value) {
        fieldValueOperation(object, name,
                (field, o) -> {
                    field.set(o, value);
                    return null; //fake return result
                }
        );
    }

    private static Object fieldValueOperation(Object object, String name, ReflectionBiFunction<Field, Object, Object> fieldFunction) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }
            return fieldFunction.apply(field, object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    public static Object callMethod(Object object, String name, Object... args) {
        CommonUtils.requiredNotNull(name, "Method name must be not null");
        Class<?>[] argsClasses = toClasses(args);
        return callMethod(object, getMethodByName(object.getClass(), name,argsClasses),
                args);
    }

    public static Object callMethod(Object obj, Method method, Object... args) {
        CommonUtils.requiredNotNull(method, "Method must be not null");
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
        CommonUtils.requiredNotNull(clazz, "Class must be not null");
        CommonUtils.requiredNotNull(methodName, "Method name mus be not null");
        try {
            return clazz.getDeclaredMethod(methodName, argsType);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isNoArgsMethod(Method method) {
        CommonUtils.requiredNotNull(method, "Method must be not null");
        return method.getGenericParameterTypes().length == 0;
    }

    private static Class<?>[] toClasses(Object[] args) {
        return (Class<?>[]) Arrays.stream(args)
                .map(Object::getClass)
                .toArray();
    }
}
