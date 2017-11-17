package ru.otus.kirillov.hw05;

import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import ru.otus.kirillov.hw05.mytest.MyTestFramework;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult.TestCaseStatus;
import ru.otus.kirillov.hw05.mytest.results.MyTestResult;
import ru.otus.kirillov.hw05.testCases.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
        testForTestCaseFullSuccess(FullSuccess1.class, "test1", "test2", "test3");
    }

    @Test
    public void testForFullSuccessTestCaseClassWithAfterAndBefore() {
        testForTestCaseFullSuccess(FullSuccessWithAfterAndBefore.class, "test1", "test2", "test3");

        assertEquals(FullSuccessWithAfterAndBefore.getMethodCallOrderHolder().size(), 15);
        List<List<String>> methodCallOrder =
                ListUtils.partition(FullSuccessWithAfterAndBefore.getMethodCallOrderHolder(), 5);

        for(List<String> lst: methodCallOrder) {
            Object[] beforeMethods = lst.subList(0, 2).toArray();
            Arrays.sort(beforeMethods);
            assertArrayEquals(
                    beforeMethods,
                    new String[] {"b1", "b2"}
            );

            Object[] afterMethods = lst.subList(3, 5).toArray();
            Arrays.sort(afterMethods);
            assertArrayEquals(
                    afterMethods,
                    new String[] {"a1", "a2"}
            );
        }
    }

    private void testForTestCaseFullSuccess(Class<?> clazz, String... methodNames) {
        Arrays.sort(methodNames);
        MyTestCaseResult result = MyTestFramework.runTestClass(getClassName(clazz));
        assertEquals(result.getStatus(), TestCaseStatus.PASSED);
        assertEquals(result.getPassedTestsCount(), methodNames.length);
        assertEquals(result.getFailedTestsCount(), 0);
        assertEquals(result.getNotPassedTests(), 0);

        List<MyTestResult> resultDetails = result.getTestResults();
        assertEquals(resultDetails.size(), methodNames.length);
        assertEquals(result.getTestCaseClass(), clazz);

        assertArrayEquals(
                resultDetails.stream().map(MyTestResult::getTestStatus).distinct().toArray(),
                new MyTestResult.TestStatus[]{MyTestResult.TestStatus.PASSED}
        );
        assertArrayEquals(
                resultDetails.stream().map(MyTestResult::getMessage).distinct().toArray(),
                new String[]{"OK"}
        );

        assertArrayEquals(
                resultDetails.stream().map(MyTestResult::getTestMethod)
                        .map(Method::getName).sorted().toArray(),
                methodNames
        );
    }

    @Test
    public void testForTestCaseClassWithPrivateConstructor() {

    }

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

    public String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

}
