package ru.otus.kirillov.adapters;

import org.omg.CORBA.Object;
import ru.otus.kirillov.SerializationContext;

/** Общий случай сериализации объектов ссылочных типов.
 * Т.Е перебором их полей
 * Created by Александр on 16.01.2018.
 */
public class CommonReferenceTypeAdapter extends AbstractTypeAdapter<Object> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return true;
    }

    @Override
    public void apply(Object value, SerializationContext context) {

    }
}
