package ru.otus.kirillov.hw02.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static ru.otus.kirillov.hw02.utils.InstrumentationUtils.getHierarchicalObjectSize;

/**
 * Created by Александр on 28.10.2017.
 */
public class ReportUtils {

    private static final int REPORT_LENGTH = 90;
    private static Random RANDOM_KEY_GENERATOR = new Random(Calendar.getInstance().getTimeInMillis());

    private ReportUtils() {
    }

    public static <T> void printObjectSize(T obj) {
        System.out.println(
                String.format("Size for object %s - %d", obj.toString(),
                        getHierarchicalObjectSize(obj))
        );
    }

    public static <T> void printEmptyArraySize(Class<T> clazz) {
        Object[] array = (Object[]) Array.newInstance(clazz, 0);
        System.out.println(
                String.format("Size for empty %s - %d", clazz,
                        getHierarchicalObjectSize(array))
        );
    }

    public static <T> void printEmptyObjectSize(Class<T> clazz) {
        T obj = instanceEmptyObject(clazz);
        System.out.println(
                String.format("Size for empty %s - %d", clazz,
                        getHierarchicalObjectSize(obj))
        );
    }

    public static <A> void printArraySizeInfo(Class<A> elementClazz, int size) {
        Object[] collection = (Object[]) Array.newInstance(elementClazz, size);
        for (int i = 0; i < size; i++) {
            collection[i] = instanceEmptyObject(elementClazz);
        }
        printContainerSizeInfo(collection, collection[0], size);
    }

    public static <C extends Collection, V>
    void printCollectionSizeInfo(Class<C> collectionClazz, Class<V> elementClazz, int size) {
        V element = instanceEmptyObject(elementClazz);
        C collection = instanceEmptyObject(collectionClazz);
        for (int i = 0; i < size; i++) {
            collection.add(instanceEmptyObject(elementClazz));
        }
        printContainerSizeInfo(collection, element, size);
    }

    public static <M extends Map, K, V>
    void printMapInfo(Class<M> mapClazz, Class<V> valueClazz, Function<V, K> mapValueToKey, int size) {
        V element = instanceEmptyObject(valueClazz);
        M map = instanceEmptyObject(mapClazz);

        for (int i = 0; i < size; i++) {
            map.put(mapValueToKey.apply(element), element);
        }
        printContainerSizeInfo(map, element, size);
    }

    public static <M extends Map, V>
    void printMapInfo(Class<M> mapClazz, Class<V> valueClazz, int size) {
        printMapInfo(mapClazz, valueClazz, (v) -> RANDOM_KEY_GENERATOR.nextInt(Integer.MAX_VALUE) , size);
    }

    public static void printRowSeparator() {
        System.out.println(StringUtils.repeat("-", REPORT_LENGTH));
    }

    public static void printDoubleRowSeparator() {
        System.out.println(StringUtils.repeat("=", REPORT_LENGTH));
    }

    public static void printLabelInCenter(String label) {
        System.out.println(String.format("%" + REPORT_LENGTH / 2 + "s", label));
    }

    public static void printBlockTitle(String title) {
        printDoubleRowSeparator();
        printLabelInCenter(title);
        printDoubleRowSeparator();
    }


    public static <T> T instanceEmptyObject(Class<T> clazz) {
        if (Object[].class.isAssignableFrom(clazz)) {
            return (T) Array.newInstance(clazz.getComponentType(), 0);
        }

        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    private static void printContainerSizeInfo(Object container, Object element, int size) {
        long elementSize = getHierarchicalObjectSize(element);
        long collectionSize = getHierarchicalObjectSize(container);

        printRowSeparator();
        System.out.println(
                String.format("Container - %s, element - %s, size - %d.",
                        container.getClass().toString(), element.getClass().toString(), size)
        );
        System.out.println(
                String.format("Total size - %d, Element size - %d, Overhead - %d, " +
                                "Overhead per element - %d.", collectionSize, elementSize,
                        collectionSize - size * elementSize,
                        Math.round((1.0 * collectionSize - size * elementSize) / size))
        );
        printRowSeparator();
    }


}
