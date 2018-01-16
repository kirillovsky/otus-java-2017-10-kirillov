package ru.otus.kirillov.adapters.containers;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.AbstractTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;

import java.util.Collection;

/** Адаптер для коллекций
 * Created by Александр on 15.01.2018.
 */
public class CollectionTypeAdapter extends AbstractTypeAdapter<Collection> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public void apply(Collection value, SerializationContext context) {
        context.getGenerator().writeStartArray();
        value.stream().forEach(context::process);
        context.getGenerator().writeEnd();
    }
}
