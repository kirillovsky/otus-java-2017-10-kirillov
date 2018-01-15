package ru.otus.kirillov;

import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.utils.CommonUtils;

import javax.json.stream.JsonGenerator;
import java.util.List;

/**
 * Контекст сериализации. Используется в процессе сериализации объекта.
 * Created by Александр on 12.01.2018.
 */
public final class SerializationContext {

    private final JsonGenerator generator;

    private final ContextAdapters adapters;

    public static SerializationContext of(JsonGenerator generator, List<TypeAdapter<?>> adapters) {
        CommonUtils.requiredNotNull(generator, adapters);
        return new SerializationContext(generator, adapters);
    }

    public SerializationContext(JsonGenerator generator, List<TypeAdapter<?>> adapters) {
        this.generator = generator;
        this.adapters = ContextAdapters.of(adapters);
    }

    public JsonGenerator getGenerator() {
        return generator;
    }

    public void process(Object src){
        adapters.process(src, this);
    }
}
