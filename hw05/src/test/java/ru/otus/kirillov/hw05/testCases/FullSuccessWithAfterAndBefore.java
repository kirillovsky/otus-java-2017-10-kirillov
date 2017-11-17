package ru.otus.kirillov.hw05.testCases;

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
public class FullSuccessWithAfterAndBefore {

    private static final List<String> methodCallOrderHolder = new ArrayList<>();

    public static List<String> getMethodCallOrderHolder() {
        return methodCallOrderHolder;
    }

    @Before
    protected void b1() {
        methodCallOrderHolder.add("b1");
    }

    @Before
    public Object b2() {
        methodCallOrderHolder.add("b2");
        return null;
    }

    @After
    private void a1() {
        methodCallOrderHolder.add("a1");
    }

    @After
    public String a2() {
        methodCallOrderHolder.add("a2");
        return "";
    }

    @Test
    public void test1() {
        methodCallOrderHolder.add("test1");
    }

    @Test
    protected Object test2() {
        methodCallOrderHolder.add("test2");
        return null;
    }

    @Test
    private String test3() {
        methodCallOrderHolder.add("test3");
        return "test3";
    }
}
