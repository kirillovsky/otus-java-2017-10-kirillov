package ru.otus.kirillov.hw05.testCases;

import org.junit.Assert;
import ru.otus.kirillov.hw05.mytest.annotations.Test;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;

/**
 * Created by Александр on 17.11.2017.
 */
@TestCase
public class FailedTestCase {

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    protected Object test2() {
        Assert.assertTrue(1 != 1);
        return null;
    }

    @Test
    private String test3() {
        return "test3";
    }

    @Test
    protected void test4() {
        throw new IllegalStateException("Ooops!!!");
    }
}
