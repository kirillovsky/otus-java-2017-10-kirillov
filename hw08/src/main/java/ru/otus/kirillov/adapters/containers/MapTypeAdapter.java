package ru.otus.kirillov.adapters.containers;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.TypeAdapter;

import java.util.Map;

/** Адаптер для мап
 * Created by Александр on 16.01.2018.
 */
public class MapTypeAdapter implements TypeAdapter<Map>{

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void apply(Map value, SerializationContext context) {
        context.getGenerator().writeStartObject();
        value.forEach((k, v) -> {
            context.getGenerator().writeKey(String.valueOf(k));
            context.process(v);
        });
        context.getGenerator().writeEnd();
    }

    @Override
    public void apply(String fieldName, Map value, SerializationContext context) {
        context.getGenerator().writeKey(fieldName);
        apply(value, context);
    }
}
