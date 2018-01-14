package ru.otus.kirillov.adapters.primitive;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.function.ToIntFunction;

/**
 * Адаптер, подходящий для всех целочисленных примитивных типов,
 * которые могут быть преобразованы без потерь точности в int
 * @param <T>
 */
class IntegerTypeAdapter<T extends Number> extends PrimitiveTypeAdapter<T> {

    private final ToIntFunction<T> intProducer;

    public IntegerTypeAdapter(Class<T> boxedClass, Class<?> unboxedClass, ToIntFunction<T> intProducer) {
        super(boxedClass, unboxedClass);
        this.intProducer = CommonUtils.retunIfNotNull(intProducer);
    }

    @Override
    public void apply(T value, SerializationContext context) {
        context.getGenerator().write(intProducer.applyAsInt(value));
    }

    @Override
    public void apply(String fieldName, T value, SerializationContext context) {
        context.getGenerator().write(fieldName, intProducer.applyAsInt(value));
    }
}
