package ru.otus.kirillov.hw05;

import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.hw05.mytest.MyTestFramework;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult.TestCaseStatus;
import ru.otus.kirillov.hw05.mytest.results.MyTestResult;
import ru.otus.kirillov.hw05.testCases.*;
import ru.otus.kirillov.hw05.testCases.hierarhicalStructure.TestCase1;
import ru.otus.kirillov.hw05.testCases.hierarhicalStructure.a.TestCase2;
import ru.otus.kirillov.hw05.testCases.hierarhicalStructure.b.b1.TestCase3;
import ru.otus.kirillov.hw05.testCases.oneClass.OneTestClass1;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Юнит тесты для MyTestFrameworkTest
 */
public class MyTestFrameworkTest {

    //region Тесты для запуска единичных тест-кейсов MyTestFramework#runTestClass
    @Test(expected = IllegalArgumentException.class)
    public void testForNotTestCaseClass() {
        MyTestFramework.runTestClass(getClassName(NonTestCaseClass.class));
    }

    @Test
    public void testForTestCaseClassWithEmptyTests1() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(EmptyTests1.class));
        assertEquals(result.getStatus(), TestCaseStatus.PASSED);
        assertEquals(result.getPassedTestsCount(), 0);
        assertEquals(result.getFailedTestsCount(), 0);
        assertEquals(result.getNotPassedTests(), 0);
        assertTrue(result.getTestResults().isEmpty());
        assertEquals(result.getTestCaseClass(), EmptyTests1.class);
    }

    @Test
    public void testForTestCaseClassWithEmptyTests2() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(EmptyTests2.class));
        assertEquals(result.getStatus(), TestCaseStatus.PASSED);
        assertEquals(result.getPassedTestsCount(), 0);
        assertEquals(result.getFailedTestsCount(), 0);
        assertEquals(result.getNotPassedTests(), 0);
        assertTrue(result.getTestResults().isEmpty());
        assertEquals(result.getTestCaseClass(), EmptyTests2.class);
    }

    @Test
    public void testForFullSuccessTestCaseClass() {
        testForTestsWithOneStatus(FullSuccess.class,
                MyTestResult.TestStatus.PASSED, "test1", "test2", "test3");
    }

    @Test
    public void testForFullSuccessTestCaseClassWithAfterAndBefore() {
        testForTestsWithOneStatus(FullSuccessWithAfterAndBefore.class,
                MyTestResult.TestStatus.PASSED, "test1", "test2", "test3");

        assertEquals(FullSuccessWithAfterAndBefore.getMethodCallOrderHolder().size(), 15);
        List<List<String>> methodCallOrder =
                ListUtils.partition(FullSuccessWithAfterAndBefore.getMethodCallOrderHolder(), 5);

        for (List<String> lst : methodCallOrder) {
            Object[] beforeMethods = lst.subList(0, 2).toArray();
            Arrays.sort(beforeMethods);
            assertArrayEquals(
                    beforeMethods,
                    new String[]{"b1", "b2"}
            );

            Object[] afterMethods = lst.subList(3, 5).toArray();
            Arrays.sort(afterMethods);
            assertArrayEquals(
                    afterMethods,
                    new String[]{"a1", "a2"}
            );
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForTestCaseClassWithPrivateConstructor() {
        MyTestFramework.runTestClass(getClassName(PrivateDefaultConstructor.class));
    }

    @Test
    public void testForNotPassedTestCase() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(NotPassedTestCase.class));
        testForTestCaseResults(result, NotPassedTestCase.class, 4, 2, 2, 0);
        testForTestsNames(result, new String[]{"test1", "test2", "test3", "test4"});
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.NOT_PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test2", "test4"}
        );
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test1", "test3"}
        );
    }

    @Test
    public void testFailedTestCase() {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(FailedTestCase.class));
        testForTestCaseResults(result, FailedTestCase.class, 4, 2, 1, 1);
        testForTestsNames(result, new String[]{"test1", "test2", "test3", "test4"});
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.FAILED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test4"}
        );
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.NOT_PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test2"}
        );
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test1", "test3"}
        );
    }

    @Test
    public void testForExceptionInBefore() {
        testForTestsWithOneStatus(ExceptionInBefore.class,
                MyTestResult.TestStatus.FAILED, "test4");

        assertArrayEquals(new String[]{"before1", "before2"},
                ExceptionInBefore.getBeforeHolder().stream()
                        .sorted().toArray()
        );

        assertArrayEquals(new String[]{"after1", "after2"},
                ExceptionInBefore.getAfterHolder().stream()
                        .sorted().toArray()
        );
    }

    @Test
    public void testForExceptionInAfter() {
        testForTestsWithOneStatus(ExceptionInAfter.class,
                MyTestResult.TestStatus.FAILED, "test1", "test2");

        assertArrayEquals(new String[]{"after1", "after1", "after2", "after2"},
                ExceptionInAfter.getAfterHolder().stream()
                        .sorted().toArray()
        );
    }

    //endregion

    //region Тесты для запуска тест-кейсов по пакетам MyTestFramework#runInPackage
    @Test
    public void testForEmptyPackage() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.emptyPackage");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testForPackageWithNonTestCaseClasses() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.allNonTestCases");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testForPackageWithOneTestClass() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.oneClass");
        assertEquals(result.size(), 1);

        MyTestCaseResult tmpResult = result.get(0);
        testForTestCaseResults(tmpResult, OneTestClass1.class, 3, 2, 1, 0);
        testForTestsNames(tmpResult, new String[]{"test1", "test2", "test3"});
        assertArrayEquals(
                tmpResult.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.NOT_PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test3"}
        );
        assertArrayEquals(
                tmpResult.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == MyTestResult.TestStatus.PASSED)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                new String[]{"test1", "test2"}
        );
    }

    @Test
    public void testForPackageWithHierarhicalStructure() {
        List<MyTestCaseResult> result =
                MyTestFramework.runInPackage("ru.otus.kirillov.hw05.testCases.hierarhicalStructure");
        assertEquals(result.size(), 3);

        Map<Class<?>, MyTestCaseResult> resultMap = result.stream()
                .collect(
                        Collectors.toMap(MyTestCaseResult::getTestCaseClass, Function.identity())
                );

        MyTestCaseResult tmpResult = resultMap.get(TestCase1.class);
        testForTestsWithOneStatus(TestCase1.class, tmpResult,
                MyTestResult.TestStatus.PASSED, "test1", "test2", "test3");

        tmpResult = resultMap.get(TestCase3.class);
        testForTestsWithOneStatus(TestCase3.class, tmpResult,
                MyTestResult.TestStatus.FAILED, "test1", "test2", "test3");

        tmpResult = resultMap.get(TestCase2.class);
        testForTestsWithOneStatus(TestCase2.class, tmpResult,
                MyTestResult.TestStatus.NOT_PASSED, "test1", "test2", "test3");

    }

    //endregion

    private String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    private void testForTestsWithOneStatus(Class<?> clazz, MyTestResult.TestStatus status, String... methodNames) {
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(clazz));
        testForTestsWithOneStatus(clazz, result, status, methodNames);
    }

    private void testForTestsWithOneStatus(Class<?> clazz, MyTestCaseResult result,
                                           MyTestResult.TestStatus status, String... methodNames) {
        testForTestsNames(result, methodNames);
        switch (status) {
            case PASSED:
                testForTestCaseResults(result, clazz, methodNames.length, methodNames.length, 0, 0);
                break;
            case NOT_PASSED:
                testForTestCaseResults(result, clazz, methodNames.length, 0, methodNames.length, 0);
                break;
            case FAILED:
                testForTestCaseResults(result, clazz, methodNames.length, 0, 0, methodNames.length);
        }

        Arrays.sort(methodNames);
        assertArrayEquals(
                result.getTestResults().stream()
                        .filter(r -> r.getTestStatus() == status)
                        .map(MyTestResult::getTestMethod)
                        .map(Method::getName)
                        .sorted().toArray(),
                methodNames
        );
    }

    private void testForTestCaseResults(MyTestCaseResult result, Class<?> clazz, int tests,
                                        int passedTestCount, int notPassedTestCount, int failedTestCount) {
        assertEquals(result.getTestCaseClass(), clazz);
        assertEquals(tests, passedTestCount + notPassedTestCount + failedTestCount);
        TestCaseStatus expectedStatus = failedTestCount > 0 ? TestCaseStatus.FAILED :
                notPassedTestCount > 0 ? TestCaseStatus.NOT_PASSED : TestCaseStatus.PASSED;
        assertEquals(expectedStatus, result.getStatus());

        assertEquals(result.getFailedTestsCount(), failedTestCount);
        assertEquals(result.getNotPassedTests(), notPassedTestCount);
        assertEquals(result.getPassedTestsCount(), passedTestCount);
        assertEquals(result.getTestResults().size(), tests);
    }

    private void testForTestsNames(MyTestCaseResult result, String... testNames) {
        assertArrayEquals(
                result.getTestResults().stream().map(MyTestResult::getTestMethod)
                        .map(Method::getName).sorted().toArray(),
                testNames
        );
    }

}
