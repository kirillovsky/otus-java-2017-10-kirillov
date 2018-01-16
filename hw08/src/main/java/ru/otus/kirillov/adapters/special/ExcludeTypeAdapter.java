package ru.otus.kirillov.adapters.special;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.utils.ReflectionUtils;

/** Адаптер для сериализации "запрещенных" классов:
 * 1. Локальных классов
 * 2. Классов-членов
 * Created by Александр on 16.01.2018.
 */
public class ExcludeTypeAdapter implements TypeAdapter<Object> {

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return ReflectionUtils.isLocalClass(clazz) || ReflectionUtils.isNonStaticMember(clazz);
    }

    @Override
    public void apply(Object value, SerializationContext context) {
        //Игнорируем
    }

    @Override
    public void apply(String fieldName, Object value, SerializationContext context) {
        //Игнорируем
    }
}
