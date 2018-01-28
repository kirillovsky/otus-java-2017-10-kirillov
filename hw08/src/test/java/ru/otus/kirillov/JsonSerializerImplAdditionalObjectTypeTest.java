package ru.otus.kirillov;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.testAdapter.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.Arrays;

/** Тест работы с новым адаптером
 * Created by Александр on 17.01.2018.
 */
public class JsonSerializerImplAdditionalObjectTypeTest {

    private static JsonSerializer serializer;
    private String jsonData;

    private static class TestField {
        LocalDateTime timeNow = LocalDateTime.parse("2018-11-17T11:48:58.571");
    }


    private static final String JSON_REPRESENTATION_TEST_FIELD = "{\"timeNow\":\"2018-11-17T11:48:58.571\"}";

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl(Arrays.asList(
                new LocalDateTimeAdapter()
        ));
    }

    @Test
    public void testSimpleSerialize() {
        LocalDateTime localDateTime = LocalDateTime.now();
        jsonData = serializer.toJson(localDateTime);
        Assert.assertEquals("\"" + localDateTime.toString() + "\"", jsonData);
    }

    @Test
    public void testSerializeAsField() {
        TestField testField = new TestField();
        jsonData = serializer.toJson(testField);
        Assert.assertEquals(JSON_REPRESENTATION_TEST_FIELD, jsonData);
    }

}