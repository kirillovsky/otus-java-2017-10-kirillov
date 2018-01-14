package ru.otus.kirillov.adapters.primitive;

import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.utils.CommonUtils;

/** Абсракция для
 * Created by Александр on 14.01.2018.
 */
public abstract class PrimitiveTypeAdapter<T> implements TypeAdapter<T> {

    private final Class<T> boxedClass;

    private final Class<?> unboxedClass;

    public PrimitiveTypeAdapter(Class<T> boxedClass, Class<?> unboxedClass) {
        this.boxedClass = CommonUtils.retunIfNotNull(boxedClass);
        this.unboxedClass = CommonUtils.retunIfNotNull(unboxedClass);
    }

    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return clazz == boxedClass || clazz == unboxedClass;
    }
}
