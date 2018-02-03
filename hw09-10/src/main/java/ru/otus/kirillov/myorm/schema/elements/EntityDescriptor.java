package ru.otus.kirillov.myorm.schema.elements;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by Александр on 29.01.2018.
 */
public class EntityDescriptor {

    private Class<? extends DataSet> entityClass;

    private String tableName;

    private List<AbstractFieldDescriptor> fieldDescriptors = new ArrayList<>();

    public static List<Pair<AbstractFieldDescriptor, Object>>
    getAllFieldValuePair(EntityDescriptor entityDescriptor, DataSet object) {
        List<Pair<AbstractFieldDescriptor, Object>> result =
                entityDescriptor.getFieldDescriptors().stream()
                        .filter(f -> !f.isSyntheticField())
                        .map(f -> Pair.of(f, ReflectionUtils.getFieldValue(f.getJavaField(), object)))
                        .collect(Collectors.toList());
        result.addAll(
                entityDescriptor.getFieldDescriptors().stream()
                        .filter(f -> f.isSyntheticField())
                        .map(f -> Pair.of(f, null))
                        .collect(Collectors.toList())
        );
        return result;
    }

    public EntityDescriptor(Class<? extends DataSet> entityClass, String tableName,
                            List<AbstractFieldDescriptor> fieldDescriptors) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.fieldDescriptors = fieldDescriptors;
    }

    public Class<? extends DataSet> getEntityClass() {
        return entityClass;
    }

    public List<AbstractFieldDescriptor> getFieldDescriptors() {
        return Collections.unmodifiableList(fieldDescriptors);
    }

    public void addSyntheticField(FieldDescriptors.SyntheticFieldDescriptor field) {
        fieldDescriptors.add(field);
    }

    public List<AbstractFieldDescriptor> getFieldDescriptors(Predicate<AbstractFieldDescriptor> fieldPredicate) {
        return getFieldDescriptors().stream()
                .filter(fieldPredicate)
                .collect(Collectors.toList());
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getFieldNames(Predicate<AbstractFieldDescriptor> fieldPredicate) {
        return getFieldDescriptors().stream()
                .filter(fieldPredicate)
                .map(f -> f.getSqlFieldName()).collect(Collectors.toList());
    }

    public FieldDescriptors.GeneratedIdFieldDescriptor getGeneratedIdField() {
        return getFieldDescriptors(f -> f.isGeneratedIdField()).stream()
                .map(f -> (FieldDescriptors.GeneratedIdFieldDescriptor) f)
                .findAny().orElseThrow(
                        () -> new RuntimeException("Not found generatedId field")
                );
    }

    public List<FieldDescriptors.OneToOneFieldDescriptor> getOneToOneFields() {
        return getFieldDescriptors(f -> f.isOneToOneField()).stream()
                .map(f -> (FieldDescriptors.OneToOneFieldDescriptor) f)
                .collect(Collectors.toList());
    }

    public List<FieldDescriptors.OneToManyFieldDescriptor> getOneToManyFields() {
        return getFieldDescriptors(f -> f.isOneToManyField()).stream()
                .map(f -> (FieldDescriptors.OneToManyFieldDescriptor) f)
                .collect(Collectors.toList());
    }

    public List<AbstractFieldDescriptor> getSqlVisibleFields() {
        return getFieldDescriptors(f -> f.isSQLVisibleFields());
    }
}
