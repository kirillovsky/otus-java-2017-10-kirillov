package ru.otus.kirillov.hw05.testCases;

import org.junit.Assert;
import ru.otus.kirillov.hw05.mytest.annotations.After;
import ru.otus.kirillov.hw05.mytest.annotations.Before;
import ru.otus.kirillov.hw05.mytest.annotations.Test;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 17.11.2017.
 */
@TestCase
public class ExceptionInBefore {

    private static final List<String> BEFORE_HOLDER = new ArrayList<>();
    private static final List<String> AFTER_HOLDER = new ArrayList<>();

    @Before
    public void failedBefore() {
        throw new SecurityException();
    }

    @Before
    public void normalBefore1() {
        BEFORE_HOLDER.add("before1");
    }

    @Before
    public void normalBefore2() {
        BEFORE_HOLDER.add("before2");
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
    protected void test4() {
        Assert.assertTrue(1 != 1);
    }

    public static List<String> getAfterHolder() {
        return AFTER_HOLDER;
    }

    public static List<String> getBeforeHolder() {
        return BEFORE_HOLDER;
    }
}
