package ru.otus.kirillov.sort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * Собственно сама быстрая сортировка
 * Created by Александр on 13.02.2018.
 */
public final class QuickSort<T> {

    public static final int INSERTION_SORT_THRESHOLD = 47;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RND = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int DEFAULT_THREAD_COUNT = 4;

    private final Comparator<T> comp;
    private Semaphore taskCountSemaphore;
    private int maxTaskCount;
    private final ExecutorService executorService;

    public QuickSort(Comparator<T> comp) {
        this(comp, DEFAULT_THREAD_COUNT);
    }

    public QuickSort(Comparator<T> comp, int threadCount) {
        this.comp = CommonUtils.returnIfNotNull(comp);
        int actualThreadCount = CommonUtils.returnIfTrue(threadCount, i -> i > 0);
        this.executorService = actualThreadCount != 1 ? Executors.newFixedThreadPool(actualThreadCount) :
                Executors.newSingleThreadExecutor();
    }


    /**
     * Запуск быстрой сортировки
     *
     * @param lst       - сортируемый массив
     * @param lowBound  - нижняя граница
     * @param highBound - верхняя граница
     */
    public void quickSort(List<T> lst, int lowBound, int highBound) {
        maxTaskCount = getMaxTaskCount(highBound - lowBound + 1);
        taskCountSemaphore = maxTaskCount > 0 ? new Semaphore(maxTaskCount) : null;

        innerQuickSort(lst, lowBound, highBound);
        awaitThreadExecutionFinished();
    }

    /**
     * Верхняя оценка кол-ва тасков (при наихудшем разбиении):
     * listSize > INSERTION_SORT_THRESHOLD ? listSize + 2 - INSERTION_SORT_THRESHOLD: 0
     *
     * @param listSize
     * @return
     */
    private int getMaxTaskCount(int listSize) {
        int result = listSize > INSERTION_SORT_THRESHOLD ? listSize + 2 - INSERTION_SORT_THRESHOLD : 0;
        LOGGER.info("Max task count - {} ", result);
        return result;
    }

    /**
     * С ожидание завершения работы все тасков основная проблема, так как
     * заранее вычислить кол-во тасков (рекурсивных вызовов выполняемых в разных потоках) на исполнении нельзя.
     * Однако, есть верхняя на кол-ов тасков.
     * n - длина сортируемого массива
     * Можно контролировать их максимальное (верхнюю оценку) кол-во тасков и в конце пытаться взять
     * это число в семафоре.
     */
    private void awaitThreadExecutionFinished() {
        if (maxTaskCount > 0) {
            acquireAllTask();
        }

    }

    private void innerQuickSort(List<T> lst, int lowBound, int highBound) {
        if (isTrimTerminalRecursion(lst, lowBound, highBound)) {
            return;
        }

        if (lowBound < highBound) {
            int partitionInd = randomPartition(lst, lowBound, highBound);
            acquireTask();
            executorService.execute(withReleaseTask(() -> innerQuickSort(lst, lowBound, partitionInd - 1)));
            acquireTask();
            executorService.execute(withReleaseTask(() -> innerQuickSort(lst, partitionInd + 1, highBound)));
        }
    }

    /**
     * Постановка выбранного опорного элемента (со случайным выбором опорного элемента)
     * на его порядковое место в итоговом отсортированном массиве
     *
     * @param lst       - сортируемый массив
     * @param lowBound  - нижняя граница включительно
     * @param highBound - верняя граница включительно
     * @return индекс опорного элемента, после его простановки на нужное место
     */
    private int randomPartition(List<T> lst, int lowBound, int highBound) {
        LOGGER.debug("Phase: partition. lo - {}, hi - {}", lowBound, highBound);
        int pivot = getPivotElement(lowBound, highBound);
        LOGGER.debug("Choose pivot element index - {}", Objects.toString(pivot));
        Collections.swap(lst, pivot, highBound);

        int result = partition(lst, lowBound, highBound);
        LOGGER.debug("Pivot element new index - {}", result);
        return result;
    }

    private int partition(List<T> lst, int lowBound, int highBound) {
        T pivotElement = lst.get(highBound);
        int i = lowBound - 1;
        for (int j = lowBound; j <= highBound - 1; j++) {
            if (comp.compare(lst.get(j), pivotElement) <= 0) {
                Collections.swap(lst, ++i, j);
            }
        }
        Collections.swap(lst, i + 1, highBound);
        return i + 1;
    }

    /**
     * Выбор индекса опорного элемента на данном интервале сортируемого массива.
     * В данной реализации выбор происходит случайным образом, для большей
     * стабильности алгоритма
     *
     * @param lowBound  - нижняя граница включительно
     * @param highBound - верняя граница включительно
     * @return значение опорного элемента
     */
    private int getPivotElement(int lowBound, int highBound) {
        return RND.nextInt(highBound - lowBound) + lowBound;
    }

    /**
     * Обрезаем концевую рекурсию
     *
     * @param lst
     * @param lowBound  - нижняя граница включительно
     * @param highBound - верняя граница включительно
     * @return true - если операция была произведена, иначе - false
     */
    private boolean isTrimTerminalRecursion(List<T> lst, int lowBound, int highBound) {
        if (highBound - lowBound > INSERTION_SORT_THRESHOLD) {
            return false;
        }
        LOGGER.debug("Try to trim terminal recursion for list size - {}, low bound - {}",
                highBound - lowBound, lowBound, highBound);
        insertionSort(lst, lowBound, highBound);
        return true;
    }

    /**
     * Простейшая сортировка вставками
     *
     * @param lst
     * @param lowBound  - нижняя граница включительно
     * @param highBound - верняя, исключая
     */
    private void insertionSort(List<T> lst, int lowBound, int highBound) {
        LOGGER.debug("Try to sort (insertion sort) list: {}", lst);
        for (int i = lowBound + 1; i <= highBound; i++) {
            for (int j = i; j > lowBound && comp(lst, j - 1, j) > 0; j--) {
                Collections.swap(lst, j - 1, j);
            }
        }
        LOGGER.debug("List sorted: {} (insertion sort)", lst);
    }

    private int comp(List<T> lst, int i, int j) {
        return comp.compare(lst.get(i), lst.get(j));
    }

    private Runnable withReleaseTask(Runnable r) {
        return () -> {
            r.run();
            taskCountSemaphore.release();
            LOGGER.info("Release one task succeed!!");
            LOGGER.debug(() -> "Running tasks - " + getRunningTasks());
        };
    }

    private void acquireTask() {
        try {
            taskCountSemaphore.acquire();
            LOGGER.info("Acquire one task succeed!!");
            LOGGER.debug(() -> "Running tasks - " + getRunningTasks());
        } catch (InterruptedException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    private void acquireAllTask() {
        try {
            taskCountSemaphore.acquire(maxTaskCount);
            LOGGER.info("Acquire all task succeed!!");
        } catch (InterruptedException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    private int getRunningTasks() {
        int availablePermits = taskCountSemaphore.availablePermits();
        return availablePermits == 0 ? 0: maxTaskCount - availablePermits;
    }

}
