package ru.otus.kirillov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.testClasses.UniversalTestObject;

/**
 * Тест для сериализации объектов.
 * Все типы тестируемых полей сериализуемого объекта
 * представлены в {@link UniversalTestObject}.
 * Данный тест также покрывает случай, когда полями объекта
 * являются ссылки на другие пользовательские объекты.
 * Created by Александр on 16.01.2018.
 */
public class JsonSerializerImplObjectTest {

    private final static UniversalTestObject UNIVERSAL_TEST_OBJECT =
            UniversalTestObject.createDefaultTestObject();

    private static Gson gson;
    private static JsonSerializer serializer;
    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }

    @Test
    public void testUniversalObject() {
        jsonData = serializer.toJson(UNIVERSAL_TEST_OBJECT);
        UniversalTestObject gsonResult = gson.fromJson(jsonData, UniversalTestObject.class);

        //Не было ли случайно сериализовано статическое поле
        Assert.assertFalse(jsonData.contains("TEZZZZ"));

        Assert.assertEquals(UNIVERSAL_TEST_OBJECT, gsonResult);
        Assert.assertNull(gsonResult.getNonStaticNestedObject());
        Assert.assertEquals(gsonResult.getTransientInt(), 0);
        Assert.assertNull(gsonResult.getTransientString());
    }

}