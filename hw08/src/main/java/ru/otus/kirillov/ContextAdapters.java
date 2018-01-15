package ru.otus.kirillov;

import ru.otus.kirillov.adapters.TerminateTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.adapters.primitive.NullObjectAdapter;

import java.util.Collections;
import java.util.List;

/** Класс, предоставляющий доступ к адаптерам текущего контекста
 * Created by Александр on 15.01.2018.
 */
public final class ContextAdapters {

    private static final TypeAdapter<?> TERMINATE_TYPE_ADAPTER = new TerminateTypeAdapter();
    private static final TypeAdapter<?> NULL_OBJECT_ADAPTER = new NullObjectAdapter();

    private final List<TypeAdapter<?>> adapters;

    private ContextAdapters(List<TypeAdapter<?>> adapters) {
        this.adapters = Collections.unmodifiableList(adapters);
    }

    public static ContextAdapters of(List<TypeAdapter<?>> adapters) {
        return new ContextAdapters(adapters);
    }


    private TypeAdapter<?> findTypeAdapterFor(Class<?> clazz) {
        return adapters.stream()
                .filter(adapter -> adapter.isApplicableForType(clazz))
                .findFirst()
                .orElse(TERMINATE_TYPE_ADAPTER);
    }

    public void process(Object obj, SerializationContext context) {
        TypeAdapter adapter = (obj == null) ? NULL_OBJECT_ADAPTER:
                findTypeAdapterFor(obj.getClass());
        adapter.apply(obj, context);
    }
}
