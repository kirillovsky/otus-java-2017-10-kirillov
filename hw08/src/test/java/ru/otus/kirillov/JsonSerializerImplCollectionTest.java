package ru.otus.kirillov;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.testClasses.TestEnum;

import java.lang.reflect.Type;
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
public class JsonSerializerImplCollectionTest {

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
        collectionTest(LONG_TEST, new TypeToken<Collection<Long>>(){}.getType());
    }

    @Test
    public void testEnum() {
        collectionTest(ENUM_TEST, new TypeToken<Collection<TestEnum>>(){}.getType());
    }

    @Test
    public void testString() {
        collectionTest(STRING_TEST);
    }

    @Test
    public void testArray() {
        jsonData = serializer.toJson(ARRAY_TEST);
        Collection<String[]> gsonCollection  = gson.fromJson(jsonData,
                new TypeToken<Collection<String[]>>(){}.getType());
        Assert.assertEquals(ARRAY_TEST.size(), gsonCollection.size());

        for(Iterator<String[]> it1 = ARRAY_TEST.iterator(), it2 = gsonCollection.iterator();
            it1.hasNext() && it2.hasNext();) {
            Assert.assertArrayEquals(it1.next(), it2.next());
        }
    }

    @Test
    public void testSomeCollectionTypes() {
        collectionTest(new HashSet<>(STRING_TEST), new TypeToken<Collection<String>>(){}.getType());

        collectionTest(new LinkedList<>(LONG_TEST), new TypeToken<Collection<Long>>(){}.getType());

        collectionTest(new ArrayList<>(ENUM_TEST), new TypeToken<Collection<TestEnum>>(){}.getType());
    }

    @Test
    public void testMultiDimCollection() {
        collectionTest(Arrays.asList(STRING_TEST), new TypeToken<Collection<Collection<String>>>(){}.getType());
    }

    public <E> void collectionTest(Collection<E> expected) {
        collectionTest(expected, Collection.class);
    }

    public <E> void collectionTest(Collection<E> expected, Type token) {
        jsonData = serializer.toJson(expected);
        Collection<E> gsonCollection = gson.fromJson(jsonData, token);
        Assert.assertEquals(expected.size(), gsonCollection.size());
        Assert.assertTrue(String.format("Collections not equal(%s, %s)", expected, gsonCollection),
                gsonCollection.containsAll(expected));
    }
}