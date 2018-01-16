package ru.otus.kirillov;

import org.apache.commons.lang3.StringUtils;
import ru.otus.kirillov.adapters.CommonReferenceTypeAdapter;
import ru.otus.kirillov.adapters.containers.ArrayTypeAdapter;
import ru.otus.kirillov.adapters.EnumTypeAdapter;
import ru.otus.kirillov.adapters.StringTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.adapters.containers.CollectionTypeAdapter;
import ru.otus.kirillov.adapters.containers.MapTypeAdapter;
import ru.otus.kirillov.adapters.primitive.PrimitiveTypesAdapters;
import ru.otus.kirillov.adapters.special.ExcludeTypeAdapter;
import ru.otus.kirillov.adapters.special.ObjectTypeAdapter;

import javax.json.Json;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Logger;

import static ru.otus.kirillov.utils.CommonUtils.getClassName;
import static ru.otus.kirillov.utils.CommonUtils.getMethodName;

/**
 * @see JsonSerializer
 * Created by Александр on 08.01.2018.
 */
public class JsonSerializerImpl implements JsonSerializer {

    private static final Logger LOGGER = Logger.getLogger(getClassName(JsonSerializerImpl.class));

    private final List<TypeAdapter<?>> excludedAdapters = Arrays.asList(
            new ExcludeTypeAdapter()
    );

    private List<TypeAdapter<?>> adapters = new ArrayList<>();

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
                new ObjectTypeAdapter(), new StringTypeAdapter(),
                new EnumTypeAdapter(), new ArrayTypeAdapter(),
                new CollectionTypeAdapter(), new MapTypeAdapter(),
                new CommonReferenceTypeAdapter()
        ));
    }

    public JsonSerializerImpl() {
        this(Collections.emptyList());
    }

    public JsonSerializerImpl(Collection<TypeAdapter<?>> usersAdapters) {
        List<TypeAdapter<?>> effectiveAdapters = new ArrayList<>();
        //Добавим исключающие адаптеры
        effectiveAdapters.addAll(excludedAdapters);
        //Далее, пользовательские
        effectiveAdapters.addAll(usersAdapters);
        //Далее, предустановленные
        effectiveAdapters.addAll(adapters);
        adapters = effectiveAdapters;
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
        String result = strOutputStream.toString();
        return StringUtils.isEmpty(result) ? null: result;
    }

    @Override
    public void toJson(Object src, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream);
        SerializationContext context = SerializationContext.of(Json.createGenerator(outputStream), adapters);
        context.process(src);
        context.getGenerator().flush();
    }
}
