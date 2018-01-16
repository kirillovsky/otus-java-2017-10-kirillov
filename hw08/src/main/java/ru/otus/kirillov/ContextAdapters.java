package ru.otus.kirillov;

import ru.otus.kirillov.adapters.special.TerminateTypeAdapter;
import ru.otus.kirillov.adapters.TypeAdapter;
import ru.otus.kirillov.adapters.special.NullObjectAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Класс, предоставляющий доступ к адаптерам текущего контекста
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

    public void process(Object obj, SerializationContext context) {
        TypeAdapter adapter = (obj == null) ? NULL_OBJECT_ADAPTER :
                adapters.stream()
                        .filter(a -> a.isApplicableForType(obj.getClass()))
                        .findFirst()
                        .orElse(TERMINATE_TYPE_ADAPTER);
        adapter.apply(obj, context);
    }
}
