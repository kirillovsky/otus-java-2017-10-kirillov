package ru.otus.kirillov.hw05.mytest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Метод, отмеченный данной аннотацией, в тест-кейс классе
 * будет вызываться до каждого метода.
 * Причем, гарантируется, что при вызов будет осуществлен до запуска каждого теста
 * (отмечен {@link Test}, в независимости от того, какие исключения
 * были выброшены в остальных методах, отмеченных данной аннотацией.
 * Если при завершении работы данного метода, отмеченного данной аннотацией,
 * с исключением, запуск тестов не имеет смысла и все они считаются "упавшими"
 * статус - {@link ru.otus.kirillov.hw05.mytest.results.MyTestResult.TestStatus#FAILED}
 * Created by Александр on 15.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Before {
}
