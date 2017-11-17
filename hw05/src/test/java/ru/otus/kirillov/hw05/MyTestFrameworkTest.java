package ru.otus.kirillov.hw05;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.kirillov.hw05.mytest.MyTestFramework;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult.TestCaseStatus;
import ru.otus.kirillov.hw05.testCases.EmptyTests1;
import ru.otus.kirillov.hw05.testCases.EmptyTests2;
import ru.otus.kirillov.hw05.testCases.FullSuccess;
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
        Assert.assertEquals(result.getPassedTestsCount(), 0);
        Assert.assertEquals(result.getFailedTestsCount(), 0);
        Assert.assertEquals(result.getNotPassedTests(), 0);
        Assert.assertTrue(result.getTestResults().isEmpty());
        Assert.assertEquals(result.getTestCaseClass(), EmptyTests1.class);
    }

    @Test
    public void testForTestCaseClassWithEmptyTests2() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(EmptyTests2.class));
        Assert.assertEquals(result.getStatus(), TestCaseStatus.PASSED);
    }

    @Test
    public void testForTestCaseClassWithFullSuccess1() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(FullSuccess.class));
        Assert.assertEquals(result.getStatus(), TestCaseStatus.PASSED);
        Assert.assertEquals(result.getPassedTestsCount(), 3);
        Assert.assertEquals(result.getFailedTestsCount(), 0);
        Assert.assertEquals(result.getNotPassedTests(), 0);

    }

    @Test
    public void testForTestCaseClassWithPrivateConstructor() {

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
