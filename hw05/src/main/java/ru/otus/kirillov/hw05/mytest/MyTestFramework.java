package ru.otus.kirillov.hw05.mytest;

import ru.otus.kirillov.hw05.mytest.execution.TestCaseExecutor;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.utils.CommonUtils;
import ru.otus.kirillov.hw05.mytest.utils.ReflectionHelper;

import java.util.List;
import java.util.stream.Collectors;

/** Точка входа в тестовый фреймворк
 * Created by Александр on 15.11.2017.
 */
public final class MyTestFramework {

    private MyTestFramework() {}

    /**
     * Запуск тест-кейса по полному имени его класса
     * @param fullClassName - полное имя (включая пакет) тест-кейс класса
     * @return результат выполнения тест-кейса
     * @throws IllegalArgumentException - в случае, если задано
     * пустое емя (fullClassName == null || fullClassName.length == 0)
     * Для остальных контрактов см. {@link TestCaseExecutor#runTestCase(Class)}
     */
    public static MyTestCaseResult runTestClass(String fullClassName) {
        CommonUtils.requiredNotEmptyString(fullClassName, "Full class name must be not null");
        return TestCaseExecutor.runTestCase(ReflectionHelper.getClass(fullClassName));
    }

    /**
     * Запуск тест-кейсов по полному имени пакета, в котором они содержаться,
     * который может являться родительским пакетом для вложенных пакетов.
     * @param fullPackageName - полное имя пакета, содержащего тест-кейс классы
     * @return список результатов выполнения тест-кейсов
     * @throws IllegalArgumentException - в случае, если задано
     * пустое емя (fullPackageName == null || fullPackageName.length == 0).
     * Данный метод, также учитывает вложенные пакеты
     * Для остальных контрактов см. {@link TestCaseExecutor#runTestCase(Class)}
     */
    public static List<MyTestCaseResult> runInPackage(String fullPackageName) {
        CommonUtils.requiredNotEmptyString(fullPackageName, "Full package name must be not null");
        return ReflectionHelper.getTestClassesFromPackage(fullPackageName).stream()
                .map(TestCaseExecutor::runTestCase)
                .collect(Collectors.toList());
    }

}
