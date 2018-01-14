package ru.otus.kirillov;

import ru.otus.kirillov.adapters.NullObjectAdapter;
import ru.otus.kirillov.adapters.SimpleTypesAdapters;
import ru.otus.kirillov.adapters.TerminateTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static ru.otus.kirillov.utils.CommonUtils.getClassName;
import static ru.otus.kirillov.utils.CommonUtils.getMethodName;

/**
 * @see JsonSerializer
 * Created by Александр on 08.01.2018.
 */
public class JsonSerializerImpl implements JsonSerializer {

    private static final Logger LOGGER = Logger.getLogger(getClassName(JsonSerializerImpl.class));

    private static final TypeAdapter<?> TERMINATE_TYPE_ADAPTER = new TerminateTypeAdapter();
    private static final TypeAdapter<?> NULL_OBJECT_ADAPTER = new NullObjectAdapter();

    private final List<TypeAdapter<?>> adapters = new ArrayList<>();

    {
        adapters.addAll(Arrays.asList(
                SimpleTypesAdapters.SHORT_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.BYTE_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.INTEGER_TYPE_ADAPTER,
                SimpleTypesAdapters.LOG_TYPE_ADAPTER,
                SimpleTypesAdapters.FLOAT_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.DOUBLE_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.CHARACTER_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.BOOLEAN_PRIMITIVE_TYPE_ADAPTER,
                SimpleTypesAdapters.STRING_TYPE_ADAPTER
        ));
    }

    @Override
    public String toJson(Object src) {
        if (src == null) return null;

        OutputStream strOutputStream = new ByteArrayOutputStream();
        try {
            toJson(src, strOutputStream);
        } catch (IOException e) {
            LOGGER.throwing(getClassName(), getMethodName(), e);
            throw new RuntimeException(e);
        }
        return strOutputStream.toString();
    }

    @Override
    public void toJson(Object src, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream);
        JsonGenerator generator = Json.createGenerator(outputStream);
        process(src, SerializationContext.of(generator, adapters));
        generator.flush();
    }

    private TypeAdapter<?> findTypeAdapterFor(Class<?> clazz) {
        return adapters.stream()
                .filter(adapter -> adapter.isApplicableForType(clazz))
                .findFirst()
                .orElse(TERMINATE_TYPE_ADAPTER);
    }

    private void process(Object obj, SerializationContext context) {
        TypeAdapter adapter = (obj == null) ? NULL_OBJECT_ADAPTER:
                findTypeAdapterFor(obj.getClass());
        adapter.apply(obj, context);
    }
}
