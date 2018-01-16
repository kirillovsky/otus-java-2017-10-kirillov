package ru.otus.kirillov.adapters.special;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.TypeAdapter;

/** Адаптер для объектов класса {@link Object}
 * Created by Александр on 16.01.2018.
 */
public class ObjectTypeAdapter implements TypeAdapter<Object> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return Object.class == clazz;
    }

    @Override
    public void apply(Object value, SerializationContext context) {
        context.getGenerator().writeStartObject();
        context.getGenerator().writeEnd();
    }

    @Override
    public void apply(String fieldName, Object value, SerializationContext context) {
        context.getGenerator().writeKey(fieldName);
        apply(value, context);
    }
}
