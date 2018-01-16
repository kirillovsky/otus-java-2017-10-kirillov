package ru.otus.kirillov;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Тест сериализации Map:
 * 0. Пустая мапа
 * 1. Боксед типы для примитивов - в качестве ключа и значения
 * 2. Строка в качестве ключа и значения
 * 3. Enum - в качестве ключа и значения
 * 4. Массивы (строк, например) - как значение
 * 5. Объект - как значение
 * 6. Коллекция - в качестве значения
 * 7. Мапа - в качестве значения
 * Created by Александр on 15.01.2018.
 */
public class JsonSerializerImplMapTest {

    private final Map<String, String> EMPTY_MAP = Collections.emptyMap();

    private final Map<Long, Double> LONG_DOUBLE_MAP =
            createMap(LongStream.rangeClosed(11L, 1_001L).mapToObj(Long::valueOf), Long::doubleValue);

    private final Map<Integer, Float> INTEGER_FLOAT_MAP =
            createMap(IntStream.rangeClosed(-111, 1_230).mapToObj(Integer::valueOf), Integer::floatValue);

    private final Map<Long, String> STRING_VALUE_MAP =
            createMap(LongStream.rangeClosed(-102L, 1_230L).mapToObj(Long::valueOf), Objects::toString);

    private final Map<String, Short> STRING_KEY_MAP =
            createMap(IntStream.rangeClosed(-10, 1000).mapToObj(Objects::toString), Short::valueOf);

    private final Map<TestEnum, String> ENUM_KEY_MAP =
            createMap(Arrays.stream(TestEnum.values()), TestEnum::name);

    private final Map<String, TestEnum> ENUM_VALUE_MAP =
            ENUM_KEY_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    private final Map<Integer, String[]> ARRAY_VALUE_MAP =
            Arrays.asList(
                    new String[]{"1", "b", "dc"},
                    new String[]{null, "w"},
                    new String[]{"tezzzz"},
                    new String[]{}
            ).stream().collect(Collectors.toMap(Arrays::deepHashCode, Function.identity()));

    // TODO: 16.01.2018 Добавить мапу для объектов

    private final Map<Integer, Collection<Long>> COLLECTIONS_MAP =
            Arrays.asList(
                    new Long[]{1L, -10L, null},
                    new Long[]{-100L, 0L, null, -3L},
                    new Long[]{}
            ).stream().map(Arrays::asList).collect(Collectors.toMap(List::hashCode, Function.identity()));

    private final Map<Integer, Map<Integer, String>> MAP_MAP =
            IntStream.range(10, 100)
                    .mapToObj(i -> Collections.singletonMap(Integer.valueOf(i), "MP." + Integer.toString(i) + "VAL"))
                    .collect(Collectors.toMap(Object::hashCode, Function.identity()));

    private static Gson gson;
    private static JsonSerializer serializer;
    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }


    private static <E, K> Map<E, K> createMap(Stream<E> stream, Function<E, K> mapper) {
        return stream.collect(Collectors.toMap(Function.identity(), mapper));
    }

    @Test
    public void testEmptyMap() {
        testMap(EMPTY_MAP, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    @Test
    public void testLongDoubleMap() {
        testMap(LONG_DOUBLE_MAP, new TypeToken<Map<Long, Double>>() {
        }.getType());
    }

    @Test
    public void testIntegerFloatMap() {
        testMap(INTEGER_FLOAT_MAP, new TypeToken<Map<Integer, Float>>() {
        }.getType());
    }

    @Test
    public void testStringValueMap() {
        testMap(STRING_VALUE_MAP, new TypeToken<Map<Long, String>>() {
        }.getType());
    }

    @Test
    public void testStringKeyMap() {
        testMap(STRING_KEY_MAP, new TypeToken<Map<String, Short>>() {
        }.getType());
    }

    @Test
    public void testEnumValueMap() {
        testMap(ENUM_VALUE_MAP, new TypeToken<Map<String, TestEnum>>() {
        }.getType());
    }

    @Test
    public void testEnumKeyMap() {
        testMap(ENUM_KEY_MAP, new TypeToken<Map<TestEnum, String>>() {
        }.getType());
    }

    @Test
    public void testCollectionMap() {
        testMap(COLLECTIONS_MAP, new TypeToken<Map<Integer, Collection<Long>>>() {
        }.getType());
    }

    @Test
    public void testMapMap() {
        testMap(MAP_MAP, new TypeToken<Map<Integer, Map<Integer, String>>>() {
        }.getType());
    }

    @Test
    public void testArrayMap() {
        jsonData = serializer.toJson(ARRAY_VALUE_MAP);
        Map<Integer, String[]> gsonResult = gson.fromJson(jsonData,
                new TypeToken<Map<Integer, String[]>>() {
                }.getType());

        Assert.assertEquals(ARRAY_VALUE_MAP.keySet(), gsonResult.keySet());
        ARRAY_VALUE_MAP.forEach(
                (k, v) -> Assert.assertArrayEquals(v, gsonResult.getOrDefault(k, null))

        );
    }

    private <K, V> void testMap(Map<K, V> map, Type type) {
        jsonData = serializer.toJson(map);
        Map<K, V> gsonResult = gson.fromJson(jsonData, type);
        Assert.assertEquals(map, gsonResult);
    }
}