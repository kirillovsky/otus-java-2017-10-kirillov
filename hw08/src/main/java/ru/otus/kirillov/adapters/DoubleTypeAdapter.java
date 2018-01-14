package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.function.ToDoubleFunction;

/**
 * Адаптер, подходящий для всех целочисленных примитивных типов,
 * которые могут быть преобразованы без потерь точности в double
 *
 * @param <T>
 */
class DoubleTypeAdapter<T extends Number> extends PrimitiveTypeAdapter<T> {

    private final ToDoubleFunction<T> doubleProducer;

    public DoubleTypeAdapter(Class<T> boxedClass, Class<?> unboxedClass, ToDoubleFunction<T> doubleProducer) {
        super(boxedClass, unboxedClass);
        this.doubleProducer = CommonUtils.retunIfNotNull(doubleProducer);
    }

    @Override
    public void apply(T value, SerializationContext context) {
        context.getGenerator().write(doubleProducer.applyAsDouble(value));
    }

    @Override
    public void apply(String fieldName, T value, SerializationContext context) {
        context.getGenerator().write(fieldName, doubleProducer.applyAsDouble(value));
    }
}
