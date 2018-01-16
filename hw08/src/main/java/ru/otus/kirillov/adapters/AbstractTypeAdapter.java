package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;

/**
 * Created by Александр on 16.01.2018.
 */
public abstract class AbstractTypeAdapter<T> implements TypeAdapter<T> {

    @Override
    public void apply(String fieldName, T value, SerializationContext context) {
        context.getGenerator().writeKey(fieldName);
        apply(value, context);
    }
}
