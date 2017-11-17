package ru.otus.kirillov.hw05.mytest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Аннотация, позволяющая отметить метод как единичный тест
 * в тест-кейс классе, помеченном аннотацией {@link TestCase}
 * Created by Александр on 15.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Test {
}
