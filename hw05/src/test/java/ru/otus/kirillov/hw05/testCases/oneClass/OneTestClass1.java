package ru.otus.kirillov.hw05.testCases.oneClass;

import org.junit.Assert;
import ru.otus.kirillov.hw05.mytest.annotations.Test;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;

/**
 * Created by Александр on 17.11.2017.
 */
@TestCase
public class OneTestClass1 {

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    protected Object test2() {
        System.out.println("test2");
        return null;
    }

    @Test
    private String test3() {
        Assert.assertTrue(1 != 1);
        return "test3";
    }
}
