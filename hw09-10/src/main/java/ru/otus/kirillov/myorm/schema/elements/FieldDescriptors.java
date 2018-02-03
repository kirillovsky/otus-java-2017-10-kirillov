package ru.otus.kirillov.myorm.schema.elements;

import ru.otus.kirillov.utils.CommonUtils;
import ru.otus.kirillov.utils.ReflectionUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.util.List;
import java.util.stream.Collectors;

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
            super(sqlFieldName, javaField, JDBCType.INTEGER);
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

        public OneToOneFieldDescriptor(Field javaField,
                                       EntityDescriptor entityDescriptor,
                                       SyntheticFieldDescriptor refIdFieldDescriptor) {
            super(null, javaField, JDBCType.BIGINT, DescriptorType.ONE_TO_ONE_FIELD);
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

        public OneToManyFieldDescriptor(Field javaField,
                                        EntityDescriptor entityDescriptor,
                                        SyntheticFieldDescriptor backRefIdFieldDescriptor) {
            super(null, javaField, null, DescriptorType.ONE_TO_MANY_FIELD);
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

        public static GeneratedIdFieldDescriptor from(Class<?> clazz) {
            List<Field> idFields = ReflectionUtils.getAllFields(clazz)
                    .filter(f -> ReflectionUtils.isAnnotated(f, Id.class))
                    .collect(Collectors.toList());

            if(idFields.size() != 1) {
                throw new RuntimeException("Id must be annotated for entity once");
            }

            Field idField = idFields.get(0);
            return new GeneratedIdFieldDescriptor(ReflectionUtils.getSqlColumnName(idField), idField);
        }

        public GeneratedIdFieldDescriptor(String sqlFieldName, Field javaField) {
            super(sqlFieldName, javaField, JDBCType.BIGINT, DescriptorType.GENERATED_ID);
        }
    }

    //TODO: Не очень хорошее решение, можно в будущем переписать
    public static AbstractSimpleTypeDescriptor createSimpleTypeFieldDescriptor(Field field) {
        CommonUtils.requiredNotNull(field);
        if(field.getType() == Integer.class || field.getType() == int.class){
            return new IntegerFieldDescriptor(ReflectionUtils.getSqlColumnName(field), field);
        } else if(field.getType() == String.class) {
            return new StringFieldDescriptor(ReflectionUtils.getSqlColumnName(field), field);
        } else {
            throw new RuntimeException("Not found FieldDescriptor for class " + field.getType().getName());
        }
    }
}
