package ru.otus.kirillov.testAdapter;

import ru.otus.kirillov.SerializationContext;
import ru.otus.kirillov.adapters.AbstractTypeAdapter;

import java.time.LocalDateTime;

/** Тестовый адаптер для нового класса пользователя
 * Created by Александр on 17.01.2018.
 */
public class LocalDateTimeAdapter extends AbstractTypeAdapter<LocalDateTime> {
    @Override
    public boolean isApplicableForType(Class<?> clazz) {
        return LocalDateTime.class == clazz;
    }

    @Override
    public void apply(LocalDateTime value, SerializationContext context) {
        context.getGenerator().write(value.toString());
    }
}
