package ru.otus.kirillov.hw02.utils;

import ru.otus.kirillov.hw02.agent.InstrumentationProvider;
import ru.otus.kirillov.hw02.agent.InstrumentationProviderLoader;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Класс-утилита для работы операций инструментации.
 * Внимание! перед началом работы требуется инициализация
 * через InstrumentationUtils#initialize
 * Created by Александр on 24.10.2017.
 */
public class InstrumentationUtils {

    private static Instrumentation currentInstrumentation;
    /**
     * Хранилище ссылок на ранее подсчитанные
     * размеры объектов в одном(!!!!) запуске
     * InstrumentationUtils#getHierarchicalObjectSize
     */
    private static ReferenceHolder referenceHolder = new ReferenceHolder();

    private InstrumentationUtils() {}

    /**
     * Внимание!!! Не тестировался
     * Инициализация утилиты для случая, когда java-агент еще не был загружен.
     * Тогда сначала, будет произведена попытка динамически его подгрузить
     * @param pathToJar - путь до jar-ника Java-агента
     */
    public static void initialize(String pathToJar) {
        if(pathToJar != null) {
            InstrumentationProviderLoader.loadAgent(pathToJar);
        }
        currentInstrumentation = InstrumentationProvider.getInstrumentation();
    }

    /**
     * Инициализация утилиты для случая, когда java-агент предварительно был загружен.
     */
    public static void initialize() {
        initialize(null);
    }

    /**
     * Подсчет занимаемого размера памяти объекта в HEAP.
     * Данный метод, кроме полей примитивных типов,
     * учитывает иерархическую структуру объекта,
     * т.е все присутствующие в нем ссылки на другие объекты
     * в том числе и массивы ссылок на объекты.
     * Агоритм:
     * 1. Очистка хранилища ранее подсчитанных ссылок
     * 2. Считаем "плоский" размер объекта - (Header + поля примитивных типов + размеры ссылок)
     * 3. Если текущий объект, является массивом ссылок, то рекурсивно считаем размеры
     * не подсчитанных ранее (!!!с попомщью хранилища ранее подсчитанных ссылок) и суммируем результаты
     * 4. Получаем ссылки на все поля объекта
     * 5. Для всех непримитивных и нестатических полей получаем ссылку на значимый объект
     * 6. Рекурсивно считаем размер объекта, на который ссылается поле на шаге 5
     * 7. Результат: сумма значений, полученных на шагах (2) + (3) + (5)
     * @param object
     * @return
     */
    public static long getHierarchicalObjectSize(final Object object) {
        referenceHolder.clear();
        return getObjectSize(object);
    }

    private static long getObjectSize(final Object object) {
        long flatSize = requireInitialized().getObjectSize(object);
        //Обработка для случая массива ссылок
        if (isObjectArray(object)) {
            flatSize += Arrays.stream((Object[]) object)
                    //Если размер объекта с такой ссылкой еще не был посчитан
                    //(В рамках объекта для которого требуется посчитать размер)
                    .filter(o -> !isAlreadyCalculatedSizeFor(o))
                    //Рекурсивно считаем размер элемента массива
                    .mapToLong(o -> getObjectSize(o))
                    .sum();
        }


        //Проходим по всем полям объекта
        flatSize += getAllFields(object.getClass()).stream()
                //Не учитываем поля примитивных типов и статические поля
                .filter(f -> !f.getType().isPrimitive() && !Modifier.isStatic(f.getModifiers()))
                //Получить значение поля
                .map(f -> getFieldValue(f, object))
                //Все как для элемента массива
                .filter(o -> !isAlreadyCalculatedSizeFor(o))
                .mapToLong(o -> getObjectSize(o))
                .sum();
        return flatSize;
    }

    private static Instrumentation requireInitialized() {
        return Objects.requireNonNull(currentInstrumentation,
                "InstrumentationUtils must be initialized at first time");
    }

    private static List<Field> getAllFields(Class<?> root) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> tmp = root; tmp != null; tmp = tmp.getSuperclass()) {
            fields.addAll(Arrays.asList(tmp.getDeclaredFields()));
        }
        return fields;
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
                    String.format("IllegalAccessException for class %s field %s", obj.getClass(), fld),
                    e);
        } finally {
            fld.setAccessible(oldAccessible);
        }
    }

    private static boolean isAlreadyCalculatedSizeFor(final Object o) {
        if (o == null || referenceHolder.contains(o)) {
            return true;
        }

        referenceHolder.add(o);
        return false;
    }

    private static boolean isObjectArray(final Object o) {
        return Object[].class.isAssignableFrom(o.getClass());
    }

}
