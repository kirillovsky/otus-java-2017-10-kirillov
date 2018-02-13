package ru.otus.kirillov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.sort.QuickSort;

import java.util.Comparator;
import java.util.List;

/** Фасад для функций сортировки
 * Created by Александр on 12.02.2018.
 */
public final class SortAlgo {

    private static final Logger LOGGER = LogManager.getLogger();

    private SortAlgo() {}

    public static <T> List<T> quickSort(List<T> list, Comparator<T> comp) {
        LOGGER.debug("Before sort: {}", list);
        new QuickSort<>(comp).quickSort(list, 0, list.size() - 1);
        LOGGER.debug("Before sort: {}", list);
        return list;
    }

    public static <T extends Comparable<? super T>> List<T> quickSort(List<T> list) {
        return quickSort(list, Comparator.naturalOrder());
    }
}
