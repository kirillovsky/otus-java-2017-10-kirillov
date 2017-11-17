package ru.otus.kirillov.hw05.mytest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Метод, отмеченный данной аннотацией, в тест-кейс классе
 * будет гарантированно вызываться после завершения каждого тестового метода,
 * в независимости от того, было ли выброшено исключение в самом тестовом методе
 * {@link Test}, в подготовительном методе {@link Before}, либо в остальных
 * методах, отмеченных данной аннотацией.
 * Если при завершении работы данного метода, отмеченного данной аннотацией,
 * с исключением, запуск тестов не имеет смысла и все они считаются "упавшими"
 * статус - {@link ru.otus.kirillov.hw05.mytest.results.MyTestResult.TestStatus#FAILED}
 * Created by Александр on 15.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface After {
}
