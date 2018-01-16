package ru.otus.kirillov.testClasses;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.otus.kirillov.testClasses.UniversalTestObject.AEnum.*;

/**
 * Универсальный тестовый класс.
 * Created by Александр on 16.01.2018.
 */
public class UniversalTestObject {

    //Вложенный нестатический класс
    public class NestedNonStaticClass {
        int i;

        public NestedNonStaticClass(int i) {
            this.i = i + 1000;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NestedNonStaticClass)) return false;

            NestedNonStaticClass that = (NestedNonStaticClass) o;

            return i == that.i;

        }

        @Override
        public int hashCode() {
            return i;
        }
    }

    //Вложенный статический класс
    public static class NestedStaticClass {
        String str;

        public NestedStaticClass(String str) {
            this.str = str + "  1223434";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NestedStaticClass)) return false;

            NestedStaticClass that = (NestedStaticClass) o;

            return str != null ? str.equals(that.str) : that.str == null;

        }

        @Override
        public int hashCode() {
            return str != null ? str.hashCode() : 0;
        }
    }

    //Вложенный статический класс
    public static class NestedEnclosureStaticClass {
        String str1;
        NestedStaticClass tezz;

        public NestedEnclosureStaticClass(String str1, NestedStaticClass tezz) {
            this.str1 = str1 + " TEST";
            this.tezz = tezz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NestedEnclosureStaticClass)) return false;

            NestedEnclosureStaticClass that = (NestedEnclosureStaticClass) o;

            if (str1 != null ? !str1.equals(that.str1) : that.str1 != null) return false;
            return tezz != null ? tezz.equals(that.tezz) : that.tezz == null;

        }

        @Override
        public int hashCode() {
            int result = str1 != null ? str1.hashCode() : 0;
            result = 31 * result + (tezz != null ? tezz.hashCode() : 0);
            return result;
        }
    }

    public enum AEnum {

        A1(1), A2(2), A3(3);

        private int a;

        AEnum(int a) {
            this.a = a;
        }
    }

    //Не сериализуем. Ибо статик!
    public static final String TEZZZZ = "TEZZZZ";

    //поля примитивных типов
    private int intVal;
    private short shortVal;
    private byte byteVal;
    private long longVal;
    private double doubleVal;
    private float floatVal;
    private boolean boolVal;

    //Пара врапперов
    private Integer intVal1;
    private Boolean boolVal1;
    private Long longVal1;

    //Массивы и строки
    private String stringVal;
    private int[] intArr;
    private float[] floatArr;
    private String[] stringArr;

    //enum
    private AEnum enumVal;
    private AEnum[] enumArr;

    //Коллекции и мапы
    private List<String> stringList;
    private Map<AEnum, String> enumStringMap;

    //Объекты. Одно из них заполним объектом класса java.lang.Object, другое его наследником
    //третье - null
    private Object objectChild;
    private Object object;
    private Object objectNull;

    //Вложенный объект (не сериализуем)
    private NestedNonStaticClass nonStaticNestedObject;

    // А вот он сериализуется
    private NestedStaticClass staticNestedObject;

    private NestedEnclosureStaticClass enclosureStaticObject;

    private transient int transientInt;
    private transient String transientString;

    //region Сгенерировано
    public int getIntVal() {
        return intVal;
    }

    public UniversalTestObject withIntVal(int intVal) {
        this.intVal = intVal;
        return this;
    }

    public short getShortVal() {
        return shortVal;
    }

    public UniversalTestObject withShortVal(short shortVal) {
        this.shortVal = shortVal;
        return this;
    }

    public byte getByteVal() {
        return byteVal;
    }

    public UniversalTestObject withByteVal(byte byteVal) {
        this.byteVal = byteVal;
        return this;
    }

    public long getLongVal() {
        return longVal;
    }

    public UniversalTestObject withLongVal(long longVal) {
        this.longVal = longVal;
        return this;
    }

    public double getDoubleVal() {
        return doubleVal;
    }

    public UniversalTestObject withDoubleVal(double doubleVal) {
        this.doubleVal = doubleVal;
        return this;
    }

    public float getFloatVal() {
        return floatVal;
    }

    public UniversalTestObject withFloatVal(float floatVal) {
        this.floatVal = floatVal;
        return this;
    }

    public boolean isBoolVal() {
        return boolVal;
    }

    public UniversalTestObject withBoolVal(boolean boolVal) {
        this.boolVal = boolVal;
        return this;
    }

    public Integer getIntVal1() {
        return intVal1;
    }

    public UniversalTestObject withIntVal1(Integer intVal1) {
        this.intVal1 = intVal1;
        return this;
    }

    public Boolean getBoolVal1() {
        return boolVal1;
    }

    public UniversalTestObject withBoolVal1(Boolean boolVal1) {
        this.boolVal1 = boolVal1;
        return this;
    }

    public Long getLongVal1() {
        return longVal1;
    }

    public UniversalTestObject withLongVal1(Long longVal1) {
        this.longVal1 = longVal1;
        return this;
    }

    public String getStringVal() {
        return stringVal;
    }

    public UniversalTestObject withStringVal(String stringVal) {
        this.stringVal = stringVal;
        return this;
    }

    public int[] getIntArr() {
        return intArr;
    }

    public UniversalTestObject withIntArr(int[] intArr) {
        this.intArr = intArr;
        return this;
    }

    public float[] getFloatArr() {
        return floatArr;
    }

    public UniversalTestObject withFloatArr(float[] floatArr) {
        this.floatArr = floatArr;
        return this;
    }

    public String[] getStringArr() {
        return stringArr;
    }

    public UniversalTestObject withStringArr(String[] stringArr) {
        this.stringArr = stringArr;
        return this;
    }

    public AEnum getEnumVal() {
        return enumVal;
    }

    public UniversalTestObject withEnumVal(AEnum enumVal) {
        this.enumVal = enumVal;
        return this;
    }

    public AEnum[] getEnumArr() {
        return enumArr;
    }

    public UniversalTestObject withEnumArr(AEnum[] enumArr) {
        this.enumArr = enumArr;
        return this;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public UniversalTestObject withStringList(List<String> stringList) {
        this.stringList = stringList;
        return this;
    }

    public Map<AEnum, String> getEnumStringMap() {
        return enumStringMap;
    }

    public UniversalTestObject withEnumStringMap(Map<AEnum, String> enumStringMap) {
        this.enumStringMap = enumStringMap;
        return this;
    }

    public Object getObjectChild() {
        return objectChild;
    }

    public UniversalTestObject withObjectChild(Object objectChild) {
        this.objectChild = objectChild;
        return this;
    }

    public Object getObject() {
        return object;
    }

    public UniversalTestObject withObject(Object object) {
        this.object = object;
        return this;
    }

    public Object getObjectNull() {
        return objectNull;
    }

    public UniversalTestObject withObjectNull(Object objectNull) {
        this.objectNull = objectNull;
        return this;
    }

    public NestedNonStaticClass getNonStaticNestedObject() {
        return nonStaticNestedObject;
    }

    public UniversalTestObject withNonStaticNestedObject(NestedNonStaticClass nonStaticNestedObject) {
        this.nonStaticNestedObject = nonStaticNestedObject;
        return this;
    }

    public UniversalTestObject withCreateNonStaticNestedObject(int arg) {
        return withNonStaticNestedObject(this.new NestedNonStaticClass(arg));
    }

    public NestedStaticClass getStaticNestedObject() {
        return staticNestedObject;
    }

    public UniversalTestObject withStaticNestedObject(NestedStaticClass staticNestedObject) {
        this.staticNestedObject = staticNestedObject;
        return this;
    }

    public NestedEnclosureStaticClass getEnclosureStaticObject() {
        return enclosureStaticObject;
    }

    public UniversalTestObject withEnclosureStaticObject(NestedEnclosureStaticClass enclosureStaticObject) {
        this.enclosureStaticObject = enclosureStaticObject;
        return this;
    }

    public int getTransientInt() {
        return transientInt;
    }

    public UniversalTestObject withTransientInt(int transientInt) {
        this.transientInt = transientInt;
        return this;
    }

    public String getTransientString() {
        return transientString;
    }

    public UniversalTestObject withTransientString(String transientString) {
        this.transientString = transientString;
        return this;
    }

    public static UniversalTestObject createDefaultTestObject() {
        return new UniversalTestObject()
                .withIntVal(LocalTime.now().toSecondOfDay())
                .withShortVal((short) 1001)
                .withByteVal((byte) 3)
                .withLongVal(LocalTime.now().toNanoOfDay())
                .withDoubleVal(1.1)
                .withFloatVal(2.3f)
                .withBoolVal(true)
                .withIntVal1(Integer.MAX_VALUE)
                .withBoolVal1(null)
                .withLongVal1(3L)
                .withStringVal("1111: { TEZZZZZ@dd")
                .withStringArr(new String[]{"12", null})
                .withIntArr(new int[]{1, 2})
                .withFloatArr(new float[]{1, 3, 0.9f, 0.3333f})
                .withEnumVal(A1)
                .withEnumArr(new AEnum[]{null, A1, A2, A3})
                .withStringList(Collections.emptyList())
                .withEnumStringMap(Collections.singletonMap(A2, "Tezzz"))
                .withObjectChild(Arrays.asList(1, 2, 3, 8))
                .withObject(new Object())
                .withObjectNull(null)
                .withCreateNonStaticNestedObject(12)
                .withStaticNestedObject(new UniversalTestObject.NestedStaticClass("1234567"))
                .withEnclosureStaticObject(new UniversalTestObject.NestedEnclosureStaticClass(
                        "abcd", new UniversalTestObject.NestedStaticClass("WOW!!!")
                ))
                .withTransientInt(123)
                .withTransientString("156782abcd");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniversalTestObject)) return false;

        UniversalTestObject that = (UniversalTestObject) o;

        if (intVal != that.intVal) return false;
        if (shortVal != that.shortVal) return false;
        if (byteVal != that.byteVal) return false;
        if (longVal != that.longVal) return false;
        if (Double.compare(that.doubleVal, doubleVal) != 0) return false;
        if (Float.compare(that.floatVal, floatVal) != 0) return false;
        if (boolVal != that.boolVal) return false;
        if (intVal1 != null ? !intVal1.equals(that.intVal1) : that.intVal1 != null) return false;
        if (boolVal1 != null ? !boolVal1.equals(that.boolVal1) : that.boolVal1 != null) return false;
        if (longVal1 != null ? !longVal1.equals(that.longVal1) : that.longVal1 != null) return false;
        if (stringVal != null ? !stringVal.equals(that.stringVal) : that.stringVal != null) return false;
        if (!Arrays.equals(intArr, that.intArr)) return false;
        if (!Arrays.equals(floatArr, that.floatArr)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(stringArr, that.stringArr)) return false;
        if (enumVal != that.enumVal) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(enumArr, that.enumArr)) return false;
        if (stringList != null ? stringList.size() != that.getStringList().size()
                || !stringList.containsAll(that.getStringList()) : that.getStringList() != null)
            return false;
        if (enumStringMap != null ? !enumStringMap.equals(that.enumStringMap) : that.enumStringMap != null)
            return false;
        if (objectChild != null ? !objectChild.equals(that.objectChild) : that.objectChild != null) return false;
        if (object != null && that.object != null ? !(Object.class == object.getClass() && Object.class == that.object.getClass())
                : that.object == null && object == null)
            return false;
        if (objectNull != null ? !objectNull.equals(that.objectNull) : that.objectNull != null) return false;
        if (staticNestedObject != null ? !staticNestedObject.equals(that.staticNestedObject) : that.staticNestedObject != null)
            return false;
        return enclosureStaticObject != null ? enclosureStaticObject.equals(that.enclosureStaticObject) : that.enclosureStaticObject == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = intVal;
        result = 31 * result + (int) shortVal;
        result = 31 * result + (int) byteVal;
        result = 31 * result + (int) (longVal ^ (longVal >>> 32));
        temp = Double.doubleToLongBits(doubleVal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (floatVal != +0.0f ? Float.floatToIntBits(floatVal) : 0);
        result = 31 * result + (boolVal ? 1 : 0);
        result = 31 * result + (intVal1 != null ? intVal1.hashCode() : 0);
        result = 31 * result + (boolVal1 != null ? boolVal1.hashCode() : 0);
        result = 31 * result + (longVal1 != null ? longVal1.hashCode() : 0);
        result = 31 * result + (stringVal != null ? stringVal.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(intArr);
        result = 31 * result + Arrays.hashCode(floatArr);
        result = 31 * result + Arrays.hashCode(stringArr);
        result = 31 * result + (enumVal != null ? enumVal.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(enumArr);
        result = 31 * result + (stringList != null ? stringList.hashCode() : 0);
        result = 31 * result + (enumStringMap != null ? enumStringMap.hashCode() : 0);
        result = 31 * result + (objectChild != null ? objectChild.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (objectNull != null ? objectNull.hashCode() : 0);
        result = 31 * result + (staticNestedObject != null ? staticNestedObject.hashCode() : 0);
        result = 31 * result + (enclosureStaticObject != null ? enclosureStaticObject.hashCode() : 0);
        return result;
    }
    //endregion
}
