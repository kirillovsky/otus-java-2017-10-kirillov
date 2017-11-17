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
 * Прямой запуск тест-кейсов.
 * Created by Александр on 15.11.2017.
 */
public final class TestCaseExecutor {

    private TestCaseExecutor() {
    }

    /**
     * Выполнение единственного тест-кейса
     * и сбор результатов его тестов
     *
     * @param testCaseClass - тест-кейс класс
     * @return результат выполнения тест-кейса
     * @throws IllegalArgumentException -
     *                                  в случае, если testCaseClass == null,
     *                                  класс testCaseClass - не был помечен аннотацией
     *                                  {@link ru.otus.kirillov.hw05.mytest.annotations.TestCase},
     *                                  либо у него отсутствует дефолтный public-конструктор.
     */
    public static MyTestCaseResult runTestCase(Class<?> testCaseClass) {
        CommonUtils.requiredNotNull(testCaseClass, "Test class must be not null");
        CommonUtils.requiredPredicate(ReflectionHelper::isTestCase, testCaseClass,
                "Test-case class must be annotated with TestCase");
        CommonUtils.requiredPredicate(ReflectionHelper::hasDefaultPublicConstructor, testCaseClass,
                "Test-case class must h" +
                        "ave default constructor");

        List<Method> beforeMethods = ReflectionHelper.getBeforeMethods(testCaseClass);
        List<Method> afterMethods = ReflectionHelper.getAfterMethods(testCaseClass);

        List<MyTestResult> testResult = ReflectionHelper.getTestMethods(testCaseClass).stream()
                .map(test -> runTest(
                        instanceTestCase(testCaseClass), test,
                        beforeMethods, afterMethods
                ))
                .collect(Collectors.toList());
        return MyTestCaseResult.getTestCaseResult(testCaseClass, testResult);
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

    /**
     * Запуск едиственного тест-метода, из набора тестов.
     *
     * @param testObject    - объект тестового класса
     * @param testMethod    - тест-метод
     * @param methodsBefore - список методов, предназначенных
     *                      для запуска перед выполнением каждого тест-метода
     * @param methodsAfter  - список методов, предназначенных
     *                      для запуска после выполнения каждого тест-метода
     * @return результат выполения тест-метода
     * @throws IllegalArgumentException - в случае, если тест-метод
     *                                  имеет какие-либо входные параметры
     */
    private static MyTestResult runTest(Object testObject, Method testMethod,
                                        List<Method> methodsBefore, List<Method> methodsAfter) {
        CommonUtils.requiredPredicate(ReflectionHelper::isNoArgsMethod, testMethod,
                "Test method must be no-args");
        MyTestResult result;
        try {
            runBeforeOrAfterMethods(testObject, methodsBefore);
            ReflectionHelper.callMethod(testObject, testMethod);
            result = MyTestResult.getPassedStatus(testMethod);
        } catch (Throwable th) {
            result = from(testMethod, th);
        }

        //For guaranteed execution of @After methods
        try {
            runBeforeOrAfterMethods(testObject, methodsAfter);
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
        BeforeOrAfterMethodsException exception = null;
        for (Method method : methods) {
            try {
                ReflectionHelper.callMethod(testObject, method);
            } catch (Throwable th) {
                exception = new BeforeOrAfterMethodsException(th);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }


}
