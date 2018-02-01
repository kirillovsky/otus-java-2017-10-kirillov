package ru.otus.kirillov.myorm.shema;

import java.lang.reflect.Field;
import java.sql.JDBCType;

/**
 * Created by Александр on 29.01.2018.
 */
public abstract class AbstractFieldDescriptor {

    public enum DescriptorType {
        GENERATED_ID,
        SIMPLE_FIELD,
        ONE_TO_ONE_FIELD,
        ONE_TO_MANY_FIELD,
        SYNTHETIC_FIELD
    }

    private String sqlFieldName;

    private JDBCType type;

    private DescriptorType descriptorType;

    private Field javaField;

    public AbstractFieldDescriptor(String sqlFieldName, Field javaField,  JDBCType type, DescriptorType descriptorType) {
        this.sqlFieldName = sqlFieldName;
        this.javaField = javaField;
        this.type = type;
        this.descriptorType = descriptorType;
    }

    public String getSqlFieldName() {
        return sqlFieldName;
    }

    public JDBCType getType() {
        return type;
    }

    public DescriptorType getDescriptorType() {
        return descriptorType;
    }

    public Field getJavaField() {
        return javaField;
    }

    public boolean isPrimitiveField() {
        return descriptorType == DescriptorType.SIMPLE_FIELD;
    }

    public boolean isOneToOneField() {
        return descriptorType == DescriptorType.ONE_TO_ONE_FIELD;
    }

    public boolean isOneToManyField() {
        return descriptorType == DescriptorType.ONE_TO_MANY_FIELD;
    }

    public boolean isGeneratedIdField() {
        return descriptorType == DescriptorType.GENERATED_ID;
    }

    public boolean isSyntheticField() {
        return descriptorType == DescriptorType.SYNTHETIC_FIELD;
    }
}
