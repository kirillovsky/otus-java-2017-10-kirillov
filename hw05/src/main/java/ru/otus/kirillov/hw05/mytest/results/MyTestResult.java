package ru.otus.kirillov.hw05.mytest.results;

import java.lang.reflect.Method;
import java.util.Objects;

import static ru.otus.kirillov.hw05.mytest.results.MyTestResult.TestStatus.*;

/** Класс, отвечающий за результат одного теста
 * Created by Александр on 15.11.2017.
 */
public class MyTestResult {

    public enum TestStatus {
        PASSED,
        NOT_PASSED,
        FAILED
    }

    private static final String SUCCESS_MSG = "OK";

    public static MyTestResult getPassedStatus(Method testMethod) {
        return new MyTestResult(testMethod, TestStatus.PASSED, SUCCESS_MSG);
    }

    public static MyTestResult getNotPassedStatus(Method testMethod, String cause) {
        return new MyTestResult(testMethod, TestStatus.NOT_PASSED, cause);
    }

    public static MyTestResult getFailedStatus(Method testMethod, String cause) {
        return new MyTestResult(testMethod, TestStatus.FAILED, cause);
    }


    private TestStatus testStatus = PASSED;
    private String message;
    private Method testMethod;

    private MyTestResult(Method testMethod, TestStatus testStatus, String message) {
        this.message = message;
        this.testStatus = testStatus;
        this.testMethod = testMethod;
    }

    public String getMessage() {
        return message;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public Method getTestMethod() {
        return testMethod;
    }
}
