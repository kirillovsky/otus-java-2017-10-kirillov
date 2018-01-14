package ru.otus.kirillov.adapters.primitive;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.TypeAdapter;

/** Null-adapter
 * Created by Александр on 14.01.2018.
 */
public class NullObjectAdapter implements TypeAdapter<Object> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return true;
    }

    @Override
    public void apply(Object value, SerializationContext context) {
        context.getGenerator().writeNull();
    }

    @Override
    public void apply(String fieldName, Object value, SerializationContext context) {
        context.getGenerator().writeNull(fieldName);
    }
}
