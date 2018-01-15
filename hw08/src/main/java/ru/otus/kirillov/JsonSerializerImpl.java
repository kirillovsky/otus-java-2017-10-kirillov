package ru.otus.kirillov;

import ru.otus.kirillov.adapters.containers.ArrayTypeAdapter;
import ru.otus.kirillov.adapters.EnumTypeAdapter;
import ru.otus.kirillov.adapters.StringTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.adapters.primitive.PrimitiveTypesAdapters;

import javax.json.Json;
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
                new EnumTypeAdapter(),
                new ArrayTypeAdapter()

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
        SerializationContext context = SerializationContext.of(Json.createGenerator(outputStream), adapters);
        context.process(src);
        context.getGenerator().flush();
    }
}
