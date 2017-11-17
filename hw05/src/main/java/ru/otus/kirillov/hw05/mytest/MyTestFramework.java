package ru.otus.kirillov.hw05.mytest;

import ru.otus.kirillov.hw05.mytest.execution.TestCaseExecutor;
import ru.otus.kirillov.hw05.mytest.results.MyTestCaseResult;
import ru.otus.kirillov.hw05.mytest.utils.CommonUtils;
import ru.otus.kirillov.hw05.mytest.utils.ReflectionHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Александр on 15.11.2017.
 */
public final class MyTestFramework {

    private MyTestFramework() {}

    public static MyTestCaseResult runTestClass(String fullClassName) {
        CommonUtils.requiredNotEmptyString(fullClassName, "Full class name must be not null");
        return TestCaseExecutor.runTestCase(ReflectionHelper.getClass(fullClassName));
    }

    public static List<MyTestCaseResult> runInPackage(String fullPackageName) {
        CommonUtils.requiredNotEmptyString(fullPackageName, "Full package name must be not null");
        return ReflectionHelper.getTestClassesFromPackage(fullPackageName).stream()
                .map(TestCaseExecutor::runTestCase)
                .collect(Collectors.toList());
    }

}
