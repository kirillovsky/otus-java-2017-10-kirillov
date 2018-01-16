package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;

/** Адаптер для enum-Ов
 * Created by Александр on 14.01.2018.
 */
public class EnumTypeAdapter extends AbstractTypeAdapter<Enum> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return clazz.isEnum();
    }

    @Override
    public void apply(Enum value, SerializationContext context) {
        context.getGenerator().write(value.name());
    }
}
