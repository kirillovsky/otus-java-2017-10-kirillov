package ru.otus.kirillov.adapters.containers;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.AbstractTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;

import java.lang.reflect.Array;

/** Адаптер для сериализации массивов
 * Created by Александр on 15.01.2018.
 */
public class ArrayTypeAdapter extends AbstractTypeAdapter<Object> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return clazz.isArray();
    }

    @Override
    public void apply(Object value, SerializationContext context) {
        context.getGenerator().writeStartArray();
        for(int i = 0; i < Array.getLength(value); i++) {
            context.process(Array.get(value, i));
        }
        context.getGenerator().writeEnd();
    }
}
