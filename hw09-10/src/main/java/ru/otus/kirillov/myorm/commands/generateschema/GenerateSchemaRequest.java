package ru.otus.kirillov.myorm.commands.generateschema;

import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.shema.elements.EntityDescriptor;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Александр on 01.02.2018.
 */
public class GenerateSchemaRequest extends Request {

    private Class<? extends DataSet> entityClass;

    private Map<Class<? extends DataSet>, EntityDescriptor> fieldDescriptorsByEntityClass
            = new HashMap<>();

    private GenerateSchemaRequest() {
        super(Type.GENERATE_SCHEMA);
    }

    public GenerateSchemaRequest(Class<? extends DataSet> entityClass) {
        this();
        this.entityClass = entityClass;
    }

    public GenerateSchemaRequest(Class<? extends DataSet> entityClass,
                                 Map<Class<? extends DataSet>, EntityDescriptor> fieldDescriptorsByEntityClass) {
        this(entityClass);
        this.fieldDescriptorsByEntityClass = CommonUtils.retunIfNotNull(fieldDescriptorsByEntityClass);
    }

    public Class<? extends DataSet> getEntityClass() {
        return entityClass;
    }

    public Map<Class<? extends DataSet>, EntityDescriptor> getFieldDescriptorsByEntityClass() {
        return fieldDescriptorsByEntityClass;
    }
}
