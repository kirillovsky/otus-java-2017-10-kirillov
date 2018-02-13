package ru.otus.kirillov;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.sort.QuickSort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Александр on 12.02.2018.
 */
public class SortAlgoTest {

    @Test
    public void testEmptyList() throws Exception {
        List<Integer> result = SortAlgo.quickSort(Collections.<Integer>emptyList());
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testOneElementList() throws Exception {
        List<Integer> result = SortAlgo.quickSort(Collections.singletonList(100500));
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).longValue(), 100500);
    }

    @Test
    public void testAllElementEqualsList() throws Exception {
        List<Integer> expected = IntStream.generate(() -> 100500)
                .limit(100).boxed()
                .collect(Collectors.toList());
        List<Integer> actual = SortAlgo.quickSort(expected);
        Assert.assertEquals(actual.size(), expected.size());
        Assert.assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testOrderedList() throws Exception {
        List<Integer> actual = SortAlgo.quickSort(IntStream.range(0, 100).boxed()
                .collect(Collectors.toList()));
        Assert.assertEquals(actual.size(), 100);
        checkListOrdering(actual, Comparator.naturalOrder());
    }

    @Test
    public void testReverseOrderedList() throws Exception {
        List<Integer> actual = SortAlgo.quickSort(IntStream.range(0, 100).boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()));
        Assert.assertEquals(actual.size(), 100);
        checkListOrdering(actual, Comparator.naturalOrder());
    }

    @Test
    public void testLessThanInsertionThreshHoldLst() throws Exception {
        testListWithSize(QuickSort.INSERTION_SORT_THRESHOLD);
    }

    @Test
    public void test10List() throws Exception {
        testListWithSize(10);
    }

    @Test
    public void test100List() throws Exception {
        testListWithSize(100);
    }


    @Test
    public void test1kList() throws Exception {
        testListWithSize(1_000);
    }

    @Test
    public void test100kList() throws Exception {
        testListWithSize(100_000);
    }

    @Test
    public void test1kkList() throws Exception {
        testListWithSize(1_000_000);
    }

    @Test
    public void test10kkList() throws Exception {
        testListWithSize(10_000_000);
    }

    @Test
    public void test20kkList() throws Exception {
        testListWithSize(20_000_000);
    }

    private void testListWithSize(int size) {
        List<Integer> result = SortAlgo.quickSort(getTestList(size));
        Assert.assertEquals(result.size(), size);
        checkListOrdering(result, Comparator.naturalOrder());
    }

    private List<Integer> getTestList(int size) {
        List<Integer> result = IntStream.range(0, size)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(result);
        return result;
    }

    private <T> void checkListOrdering(List<T> testList, Comparator<T> comp) {
        testList.stream()
                .reduce((i1, i2) -> {
                    if (comp.compare(i1, i2) > 0) {
                        throw new AssertionError(
                                String.format("Result list is disordered (left = %s, right = %s)", i1, i2)
                        );
                    }
                    return i2;
                });
    }

}