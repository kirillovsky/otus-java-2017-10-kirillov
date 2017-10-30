package ru.otus.kirillov.hw03;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ru.otus.kirillov.hw03.collection.MyArrayList;

import java.util.*;

/**
 * Created by Александр on 29.10.2017.
 */
@FixMethodOrder(MethodSorters.JVM)
public class MyArrayListTest {

    private List<String> lst = new MyArrayList<>();

    @After
    public void prepareTest() {
        lst = new MyArrayList<>();
    }

    @Test
    public void addAllToEmptyArrayList() {
        List<String> expected = Arrays.asList("12", "13", "14");
        boolean isChanged = Collections.addAll(lst, (String[])expected.toArray());

        assertTrue("MyArrayList not changed after test elements add", isChanged);
        assertEquals(lst.toString() + " is not contains " + expected.toString(),
                expected, lst);
    }

    @Test
    public void addAllToNotEmptyArrayList() {
        List<String> expected = Arrays.asList("1", "2", "3", "4", "1", "2", "3", "4");
        String[] added = {"1", "2", "3", "4"};
        Collections.addAll(lst, added);

        boolean isChanged = Collections.addAll(lst, added);
        assertTrue("MyArrayList not changed after test elements add", isChanged);
        assertEquals(lst.toString() + " is not contains " + expected.toString(),
                expected, lst);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void copyToEmptyArrayList() {
        List<String> src = Arrays.asList("1", "2", "3", "4");
        Collections.copy(lst, src);
    }

    @Test
    public void copyToNotEmptyArrayList() {
        List<String> src = Arrays.asList("1", "2", "3", "4");
        List<String> expected = new ArrayList<>(src);
        expected.add("Five");

        Collections.addAll(lst, "One", "Two", "Three", "Four", "Five");
        Collections.copy(lst, src);
        assertEquals(lst.toString() + " not equals with expected "
                + expected.toString() + " after  Collections.copy " + src.toString(),
                expected, lst);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void copyToSmallestArrayList() {
        List<String> src = Arrays.asList("1", "2", "3", "4");
        Collections.addAll(lst, "One");
        Collections.copy(lst, src);
    }

    @Test
    public void sortEmptyArrayList() {
        List<String> expected = Collections.emptyList();
        Collections.sort(lst);
        assertEquals(expected, lst);
    }

    @Test
    public void sortNotEmptyArrayList() {
        Collections.addAll(lst, "4", "3", "Tape Deck", "0", "100", "-10");
        List<String> expected = Arrays.asList("-10", "0", "100", "3", "4", "Tape Deck");
        Collections.sort(lst);
        assertEquals(lst.toString() + " not equals expected " + expected.toString(),
                expected, lst);
    }


}
