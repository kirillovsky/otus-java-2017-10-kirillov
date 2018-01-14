package ru.otus.kirillov.adapters.primitive;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.StringTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.adapters.primitive.DoubleTypeAdapter;
import ru.otus.kirillov.adapters.primitive.IntegerTypeAdapter;
import ru.otus.kirillov.adapters.primitive.PrimitiveTypeAdapter;

/**
 * Created by Александр on 14.01.2018.
 */
public final class SimpleTypesAdapters {

    private SimpleTypesAdapters() {
    }

    public static final PrimitiveTypeAdapter<Long> LOG_TYPE_ADAPTER =
            new PrimitiveTypeAdapter<Long>(long.class, Long.class) {

                @Override
                public void apply(Long value, SerializationContext context) {
                    context.getGenerator().write(value.longValue());
                }

                @Override
                public void apply(String fieldName, Long value, SerializationContext context) {
                    context.getGenerator().write(value.longValue());
                }
            };

    public static final PrimitiveTypeAdapter<Integer> INTEGER_TYPE_ADAPTER =
            new IntegerTypeAdapter<>(int.class, Integer.class, Integer::intValue);

    public static final PrimitiveTypeAdapter<Byte> BYTE_PRIMITIVE_TYPE_ADAPTER =
            new IntegerTypeAdapter<>(byte.class, Byte.class, Byte::intValue);

    public static final PrimitiveTypeAdapter<Short> SHORT_PRIMITIVE_TYPE_ADAPTER =
            new IntegerTypeAdapter<>(short.class, Short.class, Short::intValue);

    public static final PrimitiveTypeAdapter<Float> FLOAT_PRIMITIVE_TYPE_ADAPTER =
            new DoubleTypeAdapter<>(float.class, Float.class, Float::doubleValue);

    public static final PrimitiveTypeAdapter<Double> DOUBLE_PRIMITIVE_TYPE_ADAPTER =
            new DoubleTypeAdapter<>(double.class, Double.class, Double::doubleValue);

    public static final PrimitiveTypeAdapter<Boolean> BOOLEAN_PRIMITIVE_TYPE_ADAPTER =
            new PrimitiveTypeAdapter<Boolean>(boolean.class, Boolean.class) {
                @Override
                public void apply(Boolean value, SerializationContext context) {
                    context.getGenerator().write(value.booleanValue());
                }

                @Override
                public void apply(String fieldName, Boolean value, SerializationContext context) {
                    context.getGenerator().write(fieldName, value.booleanValue());
                }
            };

    public static final PrimitiveTypeAdapter<Character> CHARACTER_PRIMITIVE_TYPE_ADAPTER =
            new PrimitiveTypeAdapter<Character>(char.class, Character.class) {
                @Override
                public void apply(Character value, SerializationContext context) {
                    context.getGenerator().write(value.toString());
                }

                @Override
                public void apply(String fieldName, Character value, SerializationContext context) {
                    context.getGenerator().write(fieldName, value.toString());
                }
            };
}

