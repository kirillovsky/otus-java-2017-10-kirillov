package ru.otus.kirillov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * Проверка сериализации коллекций:
 * 1. с примитивным типом
 * 2. с enum
 * 3. со стрингой
 * 4. с массивом
 * 5. с объектами
 * 6. поработать с парой типов разных коллекций
 * 7. Хмм. Вложенные коллекции
 * Created by Александр on 15.01.2018.
 */
public class JsonSerializerImplCollectionTestTest {

    private static final Collection<Long> LONG_TEST = Arrays.asList(
            1_000_500_100_500L, -11_000_500_100_500L, 11L
    );

    private static final Collection<TestEnum> ENUM_TEST = Arrays.asList(
            TestEnum.TEST_2, TestEnum.TEST_3, TestEnum.TEST_1, TestEnum.TEST_3, null
    );

    private static final Collection<String> STRING_TEST = Arrays.asList("abczABCZ1234", "ds", null, "");

    private static final Collection<String[]> ARRAY_TEST = Arrays.asList(
            new String[]{"1", "b", "dc"},
            new String[]{null, "w"},
            new String[]{"tezzzz"},
            new String[]{}, null
    );

    // TODO: 15.01.2018 Добавить тест для проверки работы с объектами

    private static Gson gson;
    private static JsonSerializer serializer;
    private String jsonData;

    @BeforeClass
    public static void init() {
        serializer = new JsonSerializerImpl();
        gson = new Gson();
    }

    @Test
    public void testLong() {
        jsonData = serializer.toJson(LONG_TEST);
        Assert.assertEquals(LONG_TEST, gson.fromJson(jsonData, Collection.class));
    }

    @Test
    public void testEnum() {
        jsonData = serializer.toJson(ENUM_TEST);
        Assert.assertEquals(ENUM_TEST, gson.fromJson(jsonData, Collection.class));
    }

    @Test
    public void testString() {
        jsonData = serializer.toJson(STRING_TEST);
        Assert.assertEquals(STRING_TEST, gson.fromJson(jsonData, Collection.class));
    }

    @Test
    public void testArray() {
        jsonData = serializer.toJson(ARRAY_TEST);
        Collection<String[]> gsonCollection  = gson.fromJson(jsonData, Collection.class);
        Assert.assertEquals(ARRAY_TEST.size(), gsonCollection.size());

        for(Iterator<String[]> it1 = ARRAY_TEST.iterator(), it2 = gsonCollection.iterator();
            it1.hasNext() && it2.hasNext();) {
            Assert.assertArrayEquals(it1.next(), it2.next());
        }
    }

    @Test
    public void testSomeCollectionTypes() {
        jsonData = serializer.toJson(new HashSet<>(STRING_TEST));
        Assert.assertEquals(STRING_TEST, gson.fromJson(jsonData, Set.class));

        jsonData = serializer.toJson(new LinkedList<>(LONG_TEST));
        Assert.assertEquals(LONG_TEST, gson.fromJson(jsonData, List.class));

        jsonData = serializer.toJson(new ArrayList<>(ENUM_TEST));
        Assert.assertEquals(ENUM_TEST, gson.fromJson(jsonData, List.class));
    }

    @Test
    public void testMultiDimCollection() {
        jsonData = serializer.toJson(Arrays.asList(STRING_TEST));
        Assert.assertEquals(STRING_TEST, gson.fromJson(jsonData, Collection.class));
    }
}