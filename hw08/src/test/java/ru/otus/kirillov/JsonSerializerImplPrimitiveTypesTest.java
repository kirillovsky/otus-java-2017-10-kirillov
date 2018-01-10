package ru.otus.kirillov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Тест сериализации примитивных типов, строк и null
 * Created by Александр on 10.01.2018.
 */
public class JsonSerializerImplPrimitiveTypesTest {

    private static final short SHORT_TEST = 10_000;
    private static final byte BYTE_TEST = 120;

    private static final int INTEGER_TEST = 100_500_100;
    private static final long LONG_TEST = 1_000_500_100_500L;

    private static final float FLOAT_TEST = 1.12f;
    private static final double DOUBLE_TEST = 100.123;

    private static final boolean BOOLEAN_TEST = true;

    private static final char CHAR_TEST = '\b';

    private static final String EMPTY_STRING_TEST = "";
    private static final String STRING_TEST = "abczABCZ1234\t\b.%*-_\\";

    private static Gson gson;
    private static JsonSerializer serializer;

    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }


    @Test
    public void testNull() {
        jsonData = serializer.toJson(null);
        Assert.assertNull(gson.fromJson(jsonData, Object.class));
    }

    @Test
    public void testLowIntegralTypes() {
        jsonData = serializer.toJson(SHORT_TEST);
        Assert.assertEquals(SHORT_TEST, gson.fromJson(jsonData, short.class).shortValue());

        jsonData = serializer.toJson(BYTE_TEST);
        Assert.assertEquals(BYTE_TEST, gson.fromJson(jsonData, byte.class).byteValue());
    }

    @Test
    public void testHighIntegralTypes() {
        jsonData = serializer.toJson(INTEGER_TEST);
        Assert.assertEquals(INTEGER_TEST, gson.fromJson(jsonData, int.class).intValue());

        jsonData = serializer.toJson(LONG_TEST);
        Assert.assertEquals(LONG_TEST, gson.fromJson(jsonData, long.class).longValue());
    }

    @Test
    public void testRealTypes() {
        jsonData = serializer.toJson(FLOAT_TEST);
        Assert.assertEquals(FLOAT_TEST, gson.fromJson(jsonData, float.class).floatValue(), 0.0f);

        jsonData = serializer.toJson(DOUBLE_TEST);
        Assert.assertEquals(DOUBLE_TEST, gson.fromJson(jsonData, double.class).doubleValue(), 0.0);
    }

    @Test
    public void testChar() {
        jsonData = serializer.toJson(CHAR_TEST);
        Assert.assertEquals(CHAR_TEST, gson.fromJson(jsonData, char.class).charValue());
    }

    @Test
    public void testBoolean() {
        jsonData = serializer.toJson(BOOLEAN_TEST);
        Assert.assertTrue(gson.fromJson(jsonData, boolean.class).booleanValue());
    }

    @Test
    public void testString() {
        jsonData = serializer.toJson(EMPTY_STRING_TEST);
        Assert.assertEquals(EMPTY_STRING_TEST, gson.fromJson(jsonData, String.class));

        jsonData = serializer.toJson(STRING_TEST);
        Assert.assertEquals(STRING_TEST, gson.fromJson(jsonData, String.class));
    }
}