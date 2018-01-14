package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;

/** Адаптер для строк.
 * Created by Александр on 14.01.2018.
 */
public class StringTypeAdapter implements TypeAdapter<String> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return clazz == String.class;
    }

    @Override
    public void apply(String value, SerializationContext context) {
        context.getGenerator().write(value);
    }

    @Override
    public void apply(String fieldName, String value, SerializationContext context) {
        context.getGenerator().write(fieldName, value);
    }
}
