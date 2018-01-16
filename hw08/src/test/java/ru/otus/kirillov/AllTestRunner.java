package ru.otus.kirillov;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Запускалка всех тестов
 * Created by Александр on 16.01.2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        JsonSerializerImplSimpleTypesTest.class,
        JsonSerializerImplTestArraysSimpleType.class,
        JsonSerializerImplCollectionTest.class,
        JsonSerializerImplMapTest.class
})
public class AllTestRunner {
}
