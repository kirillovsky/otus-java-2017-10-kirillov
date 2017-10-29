package ru.otus.kirillov.hw02;

import ru.otus.kirillov.hw02.agent.InstrumentationProvider;
import ru.otus.kirillov.hw02.pojos.Bean;
import ru.otus.kirillov.hw02.utils.InstrumentationUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.kirillov.hw02.utils.ReportUtils.*;
import static ru.otus.kirillov.hw02.utils.ReportUtils.printCollectionSizeInfo;
import static ru.otus.kirillov.hw02.utils.ReportUtils.printEmptyObjectSize;

/** ДЗ №2. Реализация с использованием инструментирования
 * Запускал так - java -Xss10m -javaagent:instrumagent.jar ru.otus.kirillov.hw02.Main > result.txt
 * Так как в предложенном алгоритме активно используется рекурсия стек может разорвать.
 * Created by Александр on 27.10.2017.
 */
public class Main {

    private static int CONTAINER_START_SIZE = 1_000;
    private static int CONTAINER_MAX_SIZE = 11_000;
    private static int ITERATION_COUNT = 10;

    private static Class<?> CONTAINER_ELEMENT_TYPE = Bean.class;
    private static Class<? extends Collection>[] COLLECTIONS_TYPES = new Class[]{ArrayList.class, Vector.class, LinkedList.class,
            HashSet.class, TreeSet.class, LinkedHashSet.class};

    private static Class<? extends Map>[] MAP_TYPES = new Class[]{HashMap.class, TreeMap.class, ConcurrentHashMap.class,
            LinkedHashMap.class};

    public static void main(String... argv) {
        InstrumentationUtils.initialize();
        printBlockTitle("EMPTY OBJECTS");
        printEmptyObjectSize(Object.class);
        printEmptyObjectSize(String.class);
        printEmptyObjectSize(InstrumentationProvider.class);
        printEmptyObjectSize(Bean.class);

        printBlockTitle("EMPTY CONTAINERS");
        printEmptyArraySize(CONTAINER_ELEMENT_TYPE);
        for (Class<?> clazz : COLLECTIONS_TYPES) {
            printEmptyObjectSize(clazz);
        }
        for (Class<?> clazz : MAP_TYPES) {
            printEmptyObjectSize(clazz);
        }


        printBlockTitle("FULL CONTAINERS");
        for (int i = CONTAINER_START_SIZE; i < CONTAINER_MAX_SIZE;
             i += (CONTAINER_MAX_SIZE - CONTAINER_START_SIZE) / ITERATION_COUNT) {
            printBlockTitle(Integer.toString(i));
            printArraySizeInfo(CONTAINER_ELEMENT_TYPE, i);
            for (Class<? extends Collection> clazz : COLLECTIONS_TYPES) {
                printCollectionSizeInfo(clazz, CONTAINER_ELEMENT_TYPE, i);
            }
            for (Class<? extends Map> clazz: MAP_TYPES) {
                printMapInfo(clazz, CONTAINER_ELEMENT_TYPE, i);
            }

        }
        printDoubleRowSeparator();
    }
}
