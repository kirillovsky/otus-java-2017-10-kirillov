package ru.otus.kirillov.adapters;

/**
 * Интерфейс адаптеров для сериализации объектов определенных типов.
 * Created by Александр on 12.01.2018.
 */
public interface TypeAdapter {

    boolean isApplicableForType(Class<?> clazz);

    void apply();
}
