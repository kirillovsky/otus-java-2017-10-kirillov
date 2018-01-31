package ru.otus.kirillov.myorm.mapper;

import java.lang.reflect.Field;
import java.sql.JDBCType;

/**
 * Created by Александр on 29.01.2018.
 */
// TODO: 31.01.2018 Подробно описать все классы-описатели возможных полей
public final class FieldDescriptors {

    private FieldDescriptors() {
    }

    public static abstract class AbstractSimpleTypeDescriptor extends AbstractFieldDescriptor {

        public AbstractSimpleTypeDescriptor(String sqlFieldName, Field javaField, JDBCType type) {
            super(sqlFieldName, javaField, type, DescriptorType.SIMPLE_FIELD);
        }
    }

    public static class IntegerFieldDescriptor extends AbstractSimpleTypeDescriptor {

        public IntegerFieldDescriptor(String sqlFieldName, Field javaField) {
            super(sqlFieldName, javaField, JDBCType.BIGINT);
        }
    }

    public static class StringFieldDescriptor extends AbstractSimpleTypeDescriptor {

        public StringFieldDescriptor(String sqlFieldName, Field javaField) {
            super(sqlFieldName, javaField, JDBCType.VARCHAR);
        }
    }

    public static class OneToOneFieldDescriptor extends AbstractFieldDescriptor {

        private EntityDescriptor entityDescriptor;

        private SyntheticFieldDescriptor refIdFieldDescriptor;

        public OneToOneFieldDescriptor(String sqlFieldName, Field javaField,
                                       EntityDescriptor entityDescriptor,
                                       SyntheticFieldDescriptor refIdFieldDescriptor) {
            super(sqlFieldName, javaField, JDBCType.BIGINT, DescriptorType.ONE_TO_ONE_FIELD);
            this.entityDescriptor = entityDescriptor;
            this.refIdFieldDescriptor = refIdFieldDescriptor;
        }

        public EntityDescriptor getEntityDescriptor() {
            return entityDescriptor;
        }

        public SyntheticFieldDescriptor getRefIdFieldDescriptor() {
            return refIdFieldDescriptor;
        }
    }

    public static class OneToManyFieldDescriptor extends AbstractFieldDescriptor {

        private EntityDescriptor entityDescriptor;

        private SyntheticFieldDescriptor backRefIdFieldDescriptor;

        public OneToManyFieldDescriptor(String fieldName, Field javaField,
                                        EntityDescriptor entityDescriptor,
                                        SyntheticFieldDescriptor backRefIdFieldDescriptor) {
            super(fieldName, javaField, null, DescriptorType.ONE_TO_MANY_FIELD);
            this.entityDescriptor = entityDescriptor;
            this.backRefIdFieldDescriptor = backRefIdFieldDescriptor;
        }

        public EntityDescriptor getEntityDescriptor() {
            return entityDescriptor;
        }

        public SyntheticFieldDescriptor getBackRefIdFieldDescriptor() {
            return backRefIdFieldDescriptor;
        }
    }

    public static class SyntheticFieldDescriptor extends AbstractFieldDescriptor {

        public SyntheticFieldDescriptor(String fieldName) {
            super(fieldName, null, JDBCType.BIGINT, DescriptorType.SYNTHETIC_FIELD);
        }
    }

    public static class GeneratedIdFieldDescriptor extends AbstractFieldDescriptor {

        public GeneratedIdFieldDescriptor(String sqlFieldName, Field javaField) {
            super(sqlFieldName, javaField, JDBCType.BIGINT, DescriptorType.GENERATED_ID);
        }
    }
}
