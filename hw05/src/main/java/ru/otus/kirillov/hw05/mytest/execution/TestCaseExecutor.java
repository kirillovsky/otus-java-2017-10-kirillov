package ru.otus.kirillov.hw05.mytest.execution;

import ru.otus.kirillov.hw05.mytest.exceptions.BeforeOrAfterMethodsException;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.results.MyTestResult;
import ru.otus.kirillov.hw05.mytest.utils.CommonUtils;
import ru.otus.kirillov.hw05.mytest.utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Запуск тест-кейсов
 * Created by Александр on 15.11.2017.
 */
public final class TestCaseExecutor {

    private TestCaseExecutor() {}

    public static MyTestCaseResult runTestCase(Class<?> testCaseClass) {
        CommonUtils.requiredNotNull(testCaseClass, "Test class must be not null");
        CommonUtils.requiredPredicate(ReflectionHelper::isTestCase, testCaseClass,
                "Test-case class must be annotated with TestCase");
        CommonUtils.requiredPredicate(ReflectionHelper::hasDefaultPublicConstructor, testCaseClass,
                "Test-case class must have default constructor");
        MyTestCaseResult result;
        try {
            List<Method> beforeMethods = ReflectionHelper.getBeforeMethods(testCaseClass);
            List<Method> afterMethods = ReflectionHelper.getAfterMethods(testCaseClass);

            List<MyTestResult> testResult = ReflectionHelper.getTestMethods(testCaseClass).stream()
                    .map(test -> runTest(
                            instanceTestCase(testCaseClass), test,
                            beforeMethods, afterMethods
                    ))
                    .collect(Collectors.toList());
            result = MyTestCaseResult.getTestCaseResult(testCaseClass, testResult);
        } catch (Throwable th) {
            result = MyTestCaseResult.getFailedTestCaseResult(testCaseClass);
        }
        return result;
    }

    /**
     * Инстанцируется объект класса "тест"-кейсов.
     * P.S: предполагается, что у данного класса есть дефолтный конструктор,
     * иначе - не понятно каким образом его создавать
     *
     * @param clazz - тестовый класс
     * @return его инстанс
     */
    private static Object instanceTestCase(Class<?> clazz) {
        return ReflectionHelper.instantiate(clazz);
    }

    private static MyTestResult runTest(Object testObject, Method testMethod,
                                        List<Method> methodsBefore, List<Method> methodsAfter) {
        CommonUtils.requiredPredicate(ReflectionHelper::isNoArgsMethod, testMethod,
                "Test method must be no-args");
        MyTestResult result;
        try {
            runBeforeOrAfterMethods(testObject, methodsBefore);
            ReflectionHelper.callMethod(testObject, testMethod);
            runBeforeOrAfterMethods(testObject, methodsAfter);
            result = MyTestResult.getPassedStatus(testMethod);
        } catch (Throwable th) {
            result = from(testMethod, th);
        }

        return result;
    }

    private static MyTestResult from(Method method, Throwable th) {
        Throwable actualTh = th.getCause().getCause();
        return (actualTh instanceof AssertionError) ?
                MyTestResult.getNotPassedStatus(method, actualTh.getMessage()) :
                MyTestResult.getFailedStatus(method, actualTh.getMessage());
    }

    private static void runBeforeOrAfterMethods(Object testObject, List<Method> methods) {
        for (Method method : methods) {
            try {
                ReflectionHelper.callMethod(testObject, method);
            } catch (Throwable th) {
                throw new BeforeOrAfterMethodsException(th);
            }
        }
    }
}
