package ru.otus.kirillov.hw05.testCases;

import ru.otus.kirillov.hw05.mytest.annotations.After;
import ru.otus.kirillov.hw05.mytest.annotations.Before;
import ru.otus.kirillov.hw05.mytest.annotations.TestCase;

/**
 * Created by Александр on 17.11.2017.
 */
@TestCase
public class EmptyTests2 {

    @After
    @Before
    public void buzzBeforeAndAfter() {
        throw new RuntimeException();
    }

    public void helllo() {
        throw new RuntimeException();
    }
}
