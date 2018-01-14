package ru.otus.kirillov;

import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.utils.CommonUtils;

import javax.json.stream.JsonGenerator;
import java.util.Collections;
import java.util.List;

/** Контекст сериализации. Используется в процессе сериализации объекта.
 * Created by Александр on 12.01.2018.
 */
public final class SerializationContext {

    private final Object value;

    private final JsonGenerator generator;

    private final List<TypeAdapter> adapters;

    public static SerializationContext of(Object value, JsonGenerator generator, List<TypeAdapter> adapters) {
        CommonUtils.requiredNotNull(value, generator, adapters);
        return new SerializationContext(value, generator, adapters);
    }

    public SerializationContext(Object value, JsonGenerator generator, List<TypeAdapter> adapters) {
        this.value = value;
        this.generator = generator;
        this.adapters = Collections.unmodifiableList(adapters);
    }

    public Object getValue() {
        return value;
    }

    public JsonGenerator getGenerator() {
        return generator;
    }


}
