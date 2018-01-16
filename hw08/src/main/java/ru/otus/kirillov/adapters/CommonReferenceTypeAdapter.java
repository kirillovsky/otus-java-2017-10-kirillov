package ru.otus.kirillov.adapters;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.utils.ReflectionUtils;

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
        //Будем обрабатывать объект, как мапу (Имя Поля, Его Значение)
        context.process(ReflectionUtils.getSerializedFieldNameValueMap(value));
    }
}
