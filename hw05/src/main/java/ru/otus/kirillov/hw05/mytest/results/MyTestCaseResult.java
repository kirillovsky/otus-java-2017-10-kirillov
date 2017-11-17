package ru.otus.kirillov.hw05.mytest.results;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Класс, отвечающий за результат тест-кейса.
 * Created by Александр on 15.11.2017.
 */
public class MyTestCaseResult {

    public enum TestCaseStatus {
        PASSED,
        NOT_PASSED,
        FAILED
    }

    /**
     * Статический метод генерации результата выполнения тест-кейса,
     * по результатм работы его тестов
     * @param clazz - тест-кейс класс
     * @param testResults - список результатов выполнения тестов
     * @return
     */
    public static MyTestCaseResult getTestCaseResult(Class<?> clazz, List<MyTestResult> testResults) {
        Objects.requireNonNull(clazz, "Test class must be not null");
        Objects.requireNonNull(testResults, "Test results must be not null");
        return new MyTestCaseResult(clazz, testResults);
    }

    private int failedTestsCount = 0;
    private int passedTestsCount = 0;
    private int notPassedTests = 0;
    private TestCaseStatus status = TestCaseStatus.PASSED;
    private Class<?> testCaseClass;

    private List<MyTestResult> testResults;

    private MyTestCaseResult(Class<?> testCaseClass, TestCaseStatus status) {
        this.testCaseClass = testCaseClass;
        this.status = status;
    }

    private MyTestCaseResult(Class<?> testCaseClass, List<MyTestResult> testResults) {
        this.testCaseClass = testCaseClass;
        this.testResults = testResults.isEmpty() ? Collections.emptyList() : testResults;
        initSummaryFields(testResults);
    }

    public int getFailedTestsCount() {
        return failedTestsCount;
    }

    public int getNotPassedTests() {
        return notPassedTests;
    }

    public int getPassedTestsCount() {
        return passedTestsCount;
    }

    public TestCaseStatus getStatus() {
        return status;
    }

    public List<MyTestResult> getTestResults() {
        return new ArrayList<>(testResults);
    }

    public Class<?> getTestCaseClass() {
        return testCaseClass;
    }

    private void initSummaryFields(List<MyTestResult> testResults) {
        testResults.stream()
                .collect(groupingBy(MyTestResult::getTestStatus, counting()))
                .forEach(
                        (status, count) -> {
                            switch (status) {
                                case PASSED:
                                    passedTestsCount = count.intValue();
                                    break;
                                case NOT_PASSED:
                                    notPassedTests = count.intValue();
                                    changeTestCaseStatus(TestCaseStatus.NOT_PASSED);
                                    break;
                                case FAILED:
                                    failedTestsCount = count.intValue();
                                    changeTestCaseStatus(TestCaseStatus.FAILED);
                                    break;
                            }
                        }
                );
    }

    private void changeTestCaseStatus(TestCaseStatus newStatus) {
        switch (status) {
            case PASSED:
                status = newStatus;
                break;
            case NOT_PASSED:
                status = (newStatus == TestCaseStatus.FAILED) ?
                        TestCaseStatus.FAILED : status;
                break;
        }
    }

}
