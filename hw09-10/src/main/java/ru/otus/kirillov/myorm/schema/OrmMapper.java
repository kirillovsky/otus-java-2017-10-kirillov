package ru.otus.kirillov.myorm.schema;

import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;

import java.util.HashMap;
import java.util.Map;

/** Маппер Entity-объектов во внутренне представление,
 * предназначенное для удобного маппинга этих объектов в SQL-запросы
 * Created by Александр on 02.02.2018.
 */
public class OrmMapper {

    private Map<Class<? extends DataSet>, EntityDescriptor> descriptionByClass = new HashMap<>();

    public void addAll(Map<Class<? extends DataSet>, EntityDescriptor> descriptionByClass) {
        this.descriptionByClass.putAll(descriptionByClass);
    }

    public EntityDescriptor getDescription(Class<? extends DataSet> entityClazz) {
        return descriptionByClass.computeIfAbsent(entityClazz, clazz -> {
            throw new IllegalArgumentException("Not found mapping for class - " + entityClazz.getName());
        });
    }

}
