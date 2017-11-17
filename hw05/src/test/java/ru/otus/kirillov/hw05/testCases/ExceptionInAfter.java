package ru.otus.kirillov.hw05.testCases;

import org.junit.Assert;
import ru.otus.kirillov.hw05.mytest.annotations.After;
import ru.otus.kirillov.hw05.mytest.annotations.Test;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 17.11.2017.
 */
@TestCase
public class ExceptionInAfter {

    private static final List<String> AFTER_HOLDER = new ArrayList<>();

    @After
    public void failedBefore() {
        throw new SecurityException();
    }

    @After
    public void normalAfter1() {
        AFTER_HOLDER.add("after1");
    }

    @After
    public void normalAfter2() {
        AFTER_HOLDER.add("after2");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    protected Object test2() {
        Assert.assertTrue(1 != 1);
        return null;
    }

    public static List<String> getAfterHolder() {
        return AFTER_HOLDER;
    }
}
