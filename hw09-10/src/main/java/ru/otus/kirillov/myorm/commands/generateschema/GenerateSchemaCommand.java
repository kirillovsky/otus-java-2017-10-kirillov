package ru.otus.kirillov.myorm.commands.generateschema;

import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.schema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors;
import ru.otus.kirillov.myorm.schema.elements.FieldDescriptors.*;
import ru.otus.kirillov.utils.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Комманда на генерацию схемы по Entity-классу
 * Алгоритм:
 * 1. Создаем по классу {@link EntityDescriptor} с пустым списком дескрипторов полей
 * 2. Добавляем в список дескриптор поля, отвечающего за id-шник
 * 3. Добавляем поля, непомеченные {@link javax.persistence.OneToMany}, {@link javax.persistence.Id}
 * и {@link javax.persistence.OneToOne}
 * 4. Для {@link javax.persistence.OneToOne}:
 * 4.1. Получаем дескриптор Entity-класса, на который поле ссылается
 * 4.2. Добавляем в общий список полей синтетическое поле - id-шник инстанса сущности, на которую ссылаемся
 * 4.3. Добавлем поле OneToOne
 * 5. Для {@link javax.persistence.OneToMany}:
 * 5.1. Получаем дескриптор Entity-класса коллекции поля, на который ссылаются элементы данной коллекции
 * 5.2. Далее в список полей данного дескриптора добавляем - синтетическое поле, которое будет содержать
 * id-родительского инстанса сущности
 * 5.3. Добавляем поле OneToMany
 * Created by Александр on 01.02.2018.
 */
public class GenerateSchemaCommand extends AbstractCommand<GenerateSchemaRequest,
        Map<Class<? extends DataSet>, EntityDescriptor>> {

    public GenerateSchemaCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public Map<Class<? extends DataSet>, EntityDescriptor> execute(GenerateSchemaRequest rq) {
        Class<? extends DataSet> entityClass = rq.getEntityClass();

        //Если уже есть в мапе - выходим, так как обработали раньше
        if (rq.getFieldDescriptorsByEntityClass().containsKey(rq.getEntityClass())) {
            return rq.getFieldDescriptorsByEntityClass();
        }

        Map<Class<? extends DataSet>, EntityDescriptor> result = new HashMap<>(rq.getFieldDescriptorsByEntityClass());

        if (!ReflectionUtils.isAnnotated(entityClass, Entity.class)) {
            throw new RuntimeException(String.format("Entity annotation for % must be set",
                    entityClass.getName()));
        }

        List<AbstractFieldDescriptor> fieldDescriptors = new ArrayList<>();

        //1. Создаем по классу {@link EntityDescriptor} с пустым списком дескрипторов полей
        EntityDescriptor descriptor = new EntityDescriptor(entityClass,
                ReflectionUtils.getTableName(entityClass), fieldDescriptors);
        result.put(descriptor.getEntityClass(), descriptor);
        //2. Добавляем в список дескриптор поля, отвечающего за id-шник
        fieldDescriptors.add(GeneratedIdFieldDescriptor.from(entityClass));
        //3. Добавляем поля, непомеченные {@link javax.persistence.OneToMany}, {@link javax.persistence.Id}
        //и {@link javax.persistence.OneToOne}
        fieldDescriptors.addAll(createSimpleFieldDescriptors(entityClass));
        //4. Обработка javax.persistence.OneToOne
        fieldDescriptors.addAll(processOneToOneFields(result, rq));
        //5. Обработка javax.persistence.OneToMany
        fieldDescriptors.addAll(processOneToManyFields(result, rq));
        return result;
    }

    private List<AbstractSimpleTypeDescriptor> createSimpleFieldDescriptors(Class<? extends DataSet> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> ReflectionUtils.isAnnotated(f, Column.class)
                        || !ReflectionUtils.hasAnnotations(f))
                .map(FieldDescriptors::createSimpleTypeFieldDescriptor)
                .collect(Collectors.toList());
    }

    private List<AbstractFieldDescriptor> processOneToOneFields(Map<Class<? extends DataSet>, EntityDescriptor> descriptionsMap,
                                                                GenerateSchemaRequest rq) {
        List<AbstractFieldDescriptor> result = new ArrayList<>();

        ReflectionUtils.getAllFields(rq.getEntityClass())
                .filter(f -> ReflectionUtils.isAnnotated(f, OneToOne.class))
                .forEach(f -> {
                    if (!descriptionsMap.containsKey(f.getType())) {
                        descriptionsMap.putAll(
                                calcNewEntity((Class<? extends DataSet>) f.getType(), descriptionsMap)
                        );
                    }

                    EntityDescriptor refEntityDescriptor = descriptionsMap.get(f.getType());

                    SyntheticFieldDescriptor refFieldDescriptor =
                            new SyntheticFieldDescriptor(
                                    ReflectionUtils.getRefFieldName(f, OneToOne.class,
                                            refEntityDescriptor.getTableName())
                            );
                    result.add(refFieldDescriptor);

                    OneToOneFieldDescriptor oneToOneFieldDescriptor =
                            new OneToOneFieldDescriptor(f, refEntityDescriptor, refFieldDescriptor);
                    result.add(oneToOneFieldDescriptor);
                });
        return result;
    }

    private List<OneToManyFieldDescriptor> processOneToManyFields(Map<Class<? extends DataSet>, EntityDescriptor> descriptionsMap,
                                                                 GenerateSchemaRequest rq) {
        List<OneToManyFieldDescriptor> result = new ArrayList<>();

        ReflectionUtils.getAllFields(rq.getEntityClass())
                .filter(f -> ReflectionUtils.isAnnotated(f, OneToMany.class))
                .forEach(f -> {
                    Class<? extends DataSet> refClazz =
                            (Class<? extends DataSet>) ReflectionUtils.getGenericType(f);
                    if (!descriptionsMap.containsKey(refClazz)) {
                        descriptionsMap.putAll(
                                calcNewEntity(refClazz, descriptionsMap)
                        );
                    }

                    EntityDescriptor refEntityDescriptor = descriptionsMap.get(refClazz);

                    SyntheticFieldDescriptor backRefFieldDescriptor =
                            new SyntheticFieldDescriptor(
                                    ReflectionUtils.getRefFieldName(f, OneToMany.class,
                                            refEntityDescriptor.getTableName())
                            );
                    descriptionsMap.get(refClazz).addSyntheticField(backRefFieldDescriptor);

                    result.add(new OneToManyFieldDescriptor(f, refEntityDescriptor, backRefFieldDescriptor));

                });
        return result;
    }

    private Map<Class<? extends DataSet>, EntityDescriptor> calcNewEntity(Class<? extends DataSet> clazz,
                                                                          Map<Class<? extends DataSet>, EntityDescriptor> descriptionsMap) {
        return getInvoker().invoke(new GenerateSchemaRequest(clazz, descriptionsMap));
    }


}
