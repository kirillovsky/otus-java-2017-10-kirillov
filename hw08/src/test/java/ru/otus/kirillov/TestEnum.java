package ru.otus.kirillov;

/**
 * Created by Александр on 14.01.2018.
 */
public enum TestEnum {

    TEST_1("test_1"),
    TEST_2("test_2"),
    TEST_3("test_3");

    private String tstField;

    TestEnum(String tstField) {
        this.tstField = tstField;
    }

}
