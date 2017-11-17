package ru.otus.kirillov.hw05.mytest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Аннотация, позволяющая пометить класс, как набор тест-кейсов.
 * Created by Александр on 16.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestCase {
}
