package ru.otus.kirillov.hw05;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.hw05.mytest.MyTestFramework;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult.TestCaseStatus;
import ru.otus.kirillov.hw05.testCases.EmptyTests1;
import ru.otus.kirillov.hw05.testCases.EmptyTests2;
import ru.otus.kirillov.hw05.testCases.NonTestCaseClass;

import java.util.List;

/**
 * Юнит тесты для MyTestFrameworkTest
 */
public class MyTestFrameworkTest {

    @Test(expected = IllegalArgumentException.class)
    public void testForNotTestCaseClass() {
        MyTestFramework.runTestClass(getClassName(NonTestCaseClass.class));
    }

    @Test
    public void testForTestCaseClassWithEmptyTests1() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(EmptyTests1.class));
        Assert.assertEquals(result.getStatus(), TestCaseStatus.PASSED);
    }

    @Test
    public void testForTestCaseClassWithEmptyTests2() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(EmptyTests2.class));
        Assert.assertEquals(result.getStatus(), TestCaseStatus.PASSED);
    }

    @Test
    public void testForEmptyPackage() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.emptyPackage");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testForPackageWithNonTestCaseClasses() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.allNonTestCases");
        Assert.assertTrue(result.isEmpty());
    }

    public String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

}
