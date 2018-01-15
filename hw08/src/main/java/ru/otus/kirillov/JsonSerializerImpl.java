package ru.otus.kirillov;

import ru.otus.kirillov.adapters.*;
import ru.otus.kirillov.adapters.primitive.NullObjectAdapter;
import ru.otus.kirillov.adapters.primitive.PrimitiveTypesAdapters;

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
                PrimitiveTypesAdapters.SHORT_PRIMITIVE_TYPE_ADAPTER,
                PrimitiveTypesAdapters.BYTE_PRIMITIVE_TYPE_ADAPTER,
                PrimitiveTypesAdapters.INTEGER_TYPE_ADAPTER,
                PrimitiveTypesAdapters.LOG_TYPE_ADAPTER,
                PrimitiveTypesAdapters.FLOAT_PRIMITIVE_TYPE_ADAPTER,
                PrimitiveTypesAdapters.DOUBLE_PRIMITIVE_TYPE_ADAPTER,
                PrimitiveTypesAdapters.CHARACTER_PRIMITIVE_TYPE_ADAPTER,
                PrimitiveTypesAdapters.BOOLEAN_PRIMITIVE_TYPE_ADAPTER,
                new StringTypeAdapter(),
                new EnumTypeAdapter()

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
