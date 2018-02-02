package ru.otus.kirillov.myorm.commands.generatesql;

import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.shema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.shema.elements.EntityDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Александр on 30.01.2018.
 */
public class GenerateSQLRequest extends Request {

    public enum DmlType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    public static GenerateSQLRequest insertRequest(EntityDescriptor descriptor) {
        return new GenerateSQLRequest(DmlType.INSERT, descriptor.getTableName(),
                descriptor.getFieldNames(f -> f.isSQLVisibleFields()),
                Collections.emptyList());
    }

    public static GenerateSQLRequest selectRequest(EntityDescriptor descriptor,
                                                   Collection<AbstractFieldDescriptor> whereDescriptors) {
        return new GenerateSQLRequest(DmlType.SELECT, descriptor.getTableName(),
                descriptor.getFieldNames(f -> f.isSQLVisibleFields()),
                getFieldsNames(whereDescriptors)
        );
    }

    public static GenerateSQLRequest deleteRequest(EntityDescriptor descriptor,
                                                   Collection<AbstractFieldDescriptor> whereDescriptors) {
        return new GenerateSQLRequest(DmlType.DELETE, descriptor.getTableName(), Collections.emptyList(),
                getFieldsNames(whereDescriptors));
    }

    public static GenerateSQLRequest updateRequest(EntityDescriptor descriptor,
                                                   Collection<AbstractFieldDescriptor> whereDescriptors) {
        return new GenerateSQLRequest(DmlType.UPDATE, descriptor.getTableName(),
                descriptor.getFieldNames(f -> f.isPrimitiveField() || f.isSyntheticField()),
                getFieldsNames(whereDescriptors)
        );
    }

    private static List<String> getFieldsNames(Collection<AbstractFieldDescriptor> fieldDescriptors) {
        return fieldDescriptors.stream()
                .map(AbstractFieldDescriptor::getSqlFieldName)
                .collect(Collectors.toList());
    }

    private DmlType dmlType;

    private String tableName;

    private List<String> fieldsNames;

    private List<String> whereFieldsNames;

    private GenerateSQLRequest() {
        super(Type.GENERATE_SQL);
    }

    private GenerateSQLRequest(DmlType dmlType, String tableName, List<String> fieldsNames, List<String> whereFieldsNames) {
        this();
        this.dmlType = dmlType;
        this.tableName = tableName;
        this.fieldsNames = fieldsNames;
        this.whereFieldsNames = whereFieldsNames;
    }

    public DmlType getDmlType() {
        return dmlType;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getFieldsNames() {
        return Collections.unmodifiableList(fieldsNames);
    }

    public List<String> getWhereFieldsNames() {
        return whereFieldsNames;
    }
}
