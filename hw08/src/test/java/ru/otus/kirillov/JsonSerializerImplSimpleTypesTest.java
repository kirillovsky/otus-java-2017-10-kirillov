package ru.otus.kirillov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.testClasses.TestEnum;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Тест сериализации примитивных типов, строк, enum и null
 * Created by Александр on 10.01.2018.
 */
public class JsonSerializerImplSimpleTypesTest {

    enum InnerEnum {
        A, B
    }

    private final short SHORT_TEST = 10_000;
    private final byte BYTE_TEST = 120;

    private final int INTEGER_TEST = 100_500_100;
    private final long LONG_TEST = 1_000_500_100_500L;

    private final float FLOAT_TEST = 1.12f;
    private final double DOUBLE_TEST = 100.123;

    private final boolean BOOLEAN_TEST = true;

    private final char CHAR_TEST = '\b';

    private final String EMPTY_STRING_TEST = "";
    private final String STRING_TEST = "abczABCZ1234";

    private final TestEnum TEST_ENUM_1 = TestEnum.TEST_2;
    private final TestEnum TEST_ENUM_2 = TestEnum.TEST_3;
    private final InnerEnum TEST_ENUM_3 = InnerEnum.A;

    private static Gson gson;
    private static JsonSerializer serializer;

    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }

    @Test
    public void testNullInOutputStream() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        serializer.toJson(null, outputStream);
        jsonData = outputStream.toString();
        Assert.assertNull(gson.fromJson(jsonData, Object.class));
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
        jsonData = serializer.toJson(STRING_TEST);
        Assert.assertEquals(STRING_TEST, gson.fromJson(jsonData, String.class));

        jsonData = serializer.toJson(EMPTY_STRING_TEST);
        Assert.assertEquals(EMPTY_STRING_TEST, gson.fromJson(jsonData, String.class));
    }

    @Test
    public void testEnum() {
        jsonData = serializer.toJson(TEST_ENUM_1);
        Assert.assertEquals(TEST_ENUM_1, gson.fromJson(jsonData, TestEnum.class));

        jsonData = serializer.toJson(TEST_ENUM_2);
        Assert.assertEquals(TEST_ENUM_2, gson.fromJson(jsonData, TestEnum.class));

        jsonData = serializer.toJson(TEST_ENUM_3);
        Assert.assertEquals(TEST_ENUM_3, gson.fromJson(jsonData, InnerEnum.class));
    }
}