package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;

/**
 * Интерфейс адаптеров для сериализации объектов определенных типов.
 * Created by Александр on 12.01.2018.
 */
public interface TypeAdapter<T> {

    boolean isApplicableForType(Class<?> clazz);

    void apply(T value, SerializationContext context);

    void apply(String fieldName, T value, SerializationContext context);
}
