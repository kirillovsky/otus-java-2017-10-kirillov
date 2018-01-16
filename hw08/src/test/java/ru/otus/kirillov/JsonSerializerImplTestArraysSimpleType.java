package ru.otus.kirillov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Тесты для массивов простых типов (примитивные типы, enum и строки)
 * Created by Александр on 14.01.2018.
 */
public class JsonSerializerImplTestArraysSimpleType {

    private final short[] SHORT_TEST = {10_000, 10_010, 2};
    private final byte[] BYTE_TEST = {120, 12};

    private final int[] INTEGER_TEST = {100_500_100, -10};
    private final long[] LONG_TEST = {1_000_500_100_500L, -11_000_500_100_500L, 11L};

    private final float[] FLOAT_TEST = {1.12f, 2_000.12f};
    private final double[] DOUBLE_TEST = {100.123, 1_000.45};

    private final boolean[] BOOLEAN_TEST = {true, false, true, false};

    private final char[] CHAR_TEST = {'\b', 'c', 'a', 'd'};

    private final String[] STRING_TEST = {"abczABCZ1234", "ds", null, ""};

    private final TestEnum[] TEST_ENUM = {TestEnum.TEST_2, TestEnum.TEST_3, TestEnum.TEST_1, TestEnum.TEST_3, null};

    private final int[][] MULTI_DIM_PRIMITIVE_TEST = {{1, 2, 3}, null, {4, 5}, {}, null, {1}};

    private final String[][] MULTI_DIM_STRING_TEST = {{"1", "b", "dc"}, {null, "w"}, {"tezzzz"}, {}, null};

    private static Gson gson;
    private static JsonSerializer serializer;

    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }

    @Test
    public void testLowIntegralTypes() {
        jsonData = serializer.toJson(SHORT_TEST);
        Assert.assertArrayEquals(SHORT_TEST, gson.fromJson(jsonData, short[].class));

        jsonData = serializer.toJson(BYTE_TEST);
        Assert.assertArrayEquals(BYTE_TEST, gson.fromJson(jsonData, byte[].class));
    }

    @Test
    public void testHighIntegralTypes() {
        jsonData = serializer.toJson(INTEGER_TEST);
        Assert.assertArrayEquals(INTEGER_TEST, gson.fromJson(jsonData, int[].class));

        jsonData = serializer.toJson(LONG_TEST);
        Assert.assertArrayEquals(LONG_TEST, gson.fromJson(jsonData, long[].class));
    }

    @Test
    public void testRealTypes() {
        jsonData = serializer.toJson(FLOAT_TEST);
        Assert.assertArrayEquals(FLOAT_TEST, gson.fromJson(jsonData, float[].class), 0.0f);

        jsonData = serializer.toJson(DOUBLE_TEST);
        Assert.assertArrayEquals(DOUBLE_TEST, gson.fromJson(jsonData, double[].class), 0.0);
    }

    @Test
    public void testChar() {
        jsonData = serializer.toJson(CHAR_TEST);
        Assert.assertArrayEquals(CHAR_TEST, gson.fromJson(jsonData, char[].class));
    }

    @Test
    public void testBoolean() {
        jsonData = serializer.toJson(BOOLEAN_TEST);
        Assert.assertArrayEquals(BOOLEAN_TEST, gson.fromJson(jsonData, boolean[].class));
    }

    @Test
    public void testString() {
        jsonData = serializer.toJson(STRING_TEST);
        Assert.assertArrayEquals(STRING_TEST, gson.fromJson(jsonData, String[].class));
    }

    @Test
    public void testEnum() {
        jsonData = serializer.toJson(TEST_ENUM);
        Assert.assertArrayEquals(TEST_ENUM, gson.fromJson(jsonData, TestEnum[].class));
    }

    @Test
    public void testPrimitiveMultiDimArray() {
        jsonData = serializer.toJson(MULTI_DIM_PRIMITIVE_TEST);
        int[][] gsonResult = gson.fromJson(jsonData, int[][].class);

        Assert.assertEquals(MULTI_DIM_PRIMITIVE_TEST.length, gsonResult.length);
        for (int i = 0; i < MULTI_DIM_PRIMITIVE_TEST.length; i++) {
            Assert.assertArrayEquals(MULTI_DIM_PRIMITIVE_TEST[i], gsonResult[i]);
        }
    }

    @Test
    public void testStringMultiDimArray() {
        jsonData = serializer.toJson(MULTI_DIM_STRING_TEST);
        String[][] gsonResult = gson.fromJson(jsonData, String[][].class);

        Assert.assertEquals(MULTI_DIM_STRING_TEST.length, gsonResult.length);
        for (int i = 0; i < MULTI_DIM_STRING_TEST.length; i++) {
            Assert.assertArrayEquals(MULTI_DIM_STRING_TEST[i], gsonResult[i]);
        }
    }
}