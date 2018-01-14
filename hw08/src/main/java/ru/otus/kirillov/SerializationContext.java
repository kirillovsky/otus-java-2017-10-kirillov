package ru.otus.kirillov;

import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.utils.CommonUtils;

import javax.json.stream.JsonGenerator;
import java.util.Collections;
import java.util.List;

/**
 * Контекст сериализации. Используется в процессе сериализации объекта.
 * Created by Александр on 12.01.2018.
 */
public final class SerializationContext {

    private final JsonGenerator generator;

    private final List<TypeAdapter<?>> adapters;

    public static SerializationContext of(JsonGenerator generator, List<TypeAdapter<?>> adapters) {
        CommonUtils.requiredNotNull(generator, adapters);
        return new SerializationContext(generator, adapters);
    }

    public SerializationContext(JsonGenerator generator, List<TypeAdapter<?>> adapters) {
        this.generator = generator;
        this.adapters = Collections.unmodifiableList(adapters);
    }

    public JsonGenerator getGenerator() {
        return generator;
    }


    public List<TypeAdapter<?>> getAdapters() {
        return adapters;
    }
}
