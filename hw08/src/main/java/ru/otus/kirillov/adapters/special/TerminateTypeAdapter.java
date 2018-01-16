package ru.otus.kirillov.adapters.special;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.TypeAdapter;

/**
 * Адаптор-терминатор, для случая когда нет ни одного адаптера
 * для заданного класса. В случае, если есть адаптер для всех наследников
 * {@link Object} - {@link ru.otus.kirillov.adapters.CommonReferenceTypeAdapter}
 * терминатор никогда не будет достигнут
 * Created by Александр on 14.01.2018.
 */
public class TerminateTypeAdapter implements TypeAdapter<Object> {
    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return true;
    }

    @Override
    public void apply(Object value, SerializationContext context) {
        throw new IllegalStateException("Нет адаптеров, позволяющих обработать объект: " +
                (value == null ? null : String.format("класс - %s, объект - %s", value.getClass(), value))
        );
    }

    @Override
    public void apply(String fieldName, Object value, SerializationContext context) {
        throw new IllegalStateException("Нет адаптеров, позволяющих обработать объект для поля '" + fieldName + "'. " +
                (value == null ? null : String.format("класс - %s, объект - %s", value.getClass(), value))
        );
    }
}
