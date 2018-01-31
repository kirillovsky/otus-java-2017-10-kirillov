package ru.otus.kirillov.myorm.commands.select;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.generatesql.GenerateSQLRequest;
import ru.otus.kirillov.myorm.executors.SelectExecutor;
import ru.otus.kirillov.myorm.mapper.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.mapper.EntityDescriptor;
import ru.otus.kirillov.myorm.mapper.FieldDescriptors.OneToOneFieldDescriptor;
import ru.otus.kirillov.myorm.mapper.FieldDescriptors.OneToManyFieldDescriptor;
import ru.otus.kirillov.utils.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Алгоритм:
 * 1. Генерируем запрос (без учета OneToMany)
 * 2. Исполняем его - на выходе мапа
 * 3. Обрабатываем OneToOne:
 * 3.1. Селектим объект из связанной таблицы, по id - полученному из синтетического поля для OneToOne
 * 3.2. Удаляем из мапы полей данные о данном синтетическом поле
 * 3.3. Заносим в мапу объект, полученный из результата
 * 4. Обрабатываем OneToMany:
 * 4.1. Со своим id селектим синтетическое поле из сущности, на которое ссылаемся
 * 4.2. Заносим его в мапу, по соответствующему полю
 * 5. Из мапы формируем результирующий объект
 * 5* - уточнение, необходимо обрабатывать коллекции особым образом! - хотя бы самым примитивным
 * (только для Collection, Set и List)
 * Created by Александр on 30.01.2018.
 */
public class SelectCommand extends AbstractCommand<SelectRequest, List<? extends DataSet>> {

    public SelectCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public List<? extends DataSet> execute(SelectRequest request) {
        //1. Генерируем запрос (без учета OneToMany)
        GenerateSQLRequest generatedRequest = createGenerateRequest(request);
        //2. Исполняем его - на выходе мапа
        String query = createQuery(generatedRequest);
        List<Map<AbstractFieldDescriptor, Object>> selectionResults =
                new SelectExecutor(getConnection()).execute(query,
                        request.getEntityDescriptor().getFieldDescriptors(f -> f.isPrimitiveField()),
                        request.getWhereClojure()
                );

        List<DataSet> result = new ArrayList<>();

        for (Map<AbstractFieldDescriptor, Object> resultObjectFields : selectionResults) {
            //3. Обрабатываем OneToOne:
            processOneToOneRefs(resultObjectFields, request.getEntityDescriptor());
            //4. Обрабатываем OneToMany:
            processOneToManyRefs(resultObjectFields, request.getEntityDescriptor());
            //5. Мапа сформирована, можно из нее создавать результирующий объект
            result.add(buildResultObject(request.getEntityDescriptor(), resultObjectFields));
        }


        return result;
    }

    private String createQuery(GenerateSQLRequest request) {
        return getInvoker().invoke(request);
    }

    private GenerateSQLRequest createGenerateRequest(SelectRequest request) {
        return GenerateSQLRequest.selectRequest(
                request.getEntityDescriptor(),
                request.getWhereClojure().stream()
                        .map(Pair::getKey)
                        .collect(Collectors.toList())
        );
    }

    private void processOneToOneRefs(Map<AbstractFieldDescriptor, Object> selectionResult,
                                     EntityDescriptor descriptor) {
        descriptor.getOneToOneFields()
                .forEach(f -> {
                    //3.1. Селектим объект из связанной таблицы, по id - полученному из синтетического поля для OneToOne
                    DataSet result = getOneToOneRefObject(f, selectionResult.get(f.getRefIdFieldDescriptor()));
                    //3.2. Удаляем из мапы полей данные о данном синтетическом поле
                    selectionResult.remove(f.getRefIdFieldDescriptor());
                    //3.3. Заносим в мапу объект, полученный из результата
                    selectionResult.put(f, result);
                });
    }

    private DataSet getOneToOneRefObject(OneToOneFieldDescriptor oneToOneField, Object refIdVal) {
        EntityDescriptor refEntityDescriptor = oneToOneField.getEntityDescriptor();
        List<? extends DataSet> result = getInvoker().invoke(new SelectRequest(refEntityDescriptor,
                Collections.singletonList(
                        Pair.of(refEntityDescriptor.getGeneratedIdField(), refIdVal)
                )));
        return result.get(0);
    }

    private void processOneToManyRefs(Map<AbstractFieldDescriptor, Object> selectionResult,
                                      EntityDescriptor descriptor) {
        Object entityId = selectionResult.get(descriptor.getGeneratedIdField());
        descriptor.getOneToManyFields()
                .forEach(f -> {
                    //4.1. Со своим id селектим синтетическое поле из сущности, на которое ссылаемся
                    List<? extends DataSet> result = getOneToManyRefs(f, entityId);
                    //4.2. Заносим его в мапу, по соответствующему полю
                    selectionResult.put(f, result);
                });
    }

    private List<? extends DataSet> getOneToManyRefs(OneToManyFieldDescriptor oneToManyField, Object entityId) {
        List<? extends DataSet> result = getInvoker().invoke(new SelectRequest(oneToManyField.getEntityDescriptor(),
                Collections.singletonList(
                        Pair.of(oneToManyField.getBackRefIdFieldDescriptor(), entityId)
                )));
        return result;
    }

    private DataSet buildResultObject(EntityDescriptor descriptor, Map<AbstractFieldDescriptor, Object> fieldValues) {
        boolean syntheticFieldPresent = fieldValues.keySet().stream()
                .filter(f -> f.isSyntheticField())
                .findAny().isPresent();

        if(syntheticFieldPresent) {
            throw new RuntimeException(
                    String.format("In result map\n(%s)\nretains synthetic field", fieldValues)
            );
        }


        DataSet result = ReflectionUtils.instantiate(descriptor.getClass());
        fieldValues.forEach(
                (k, v) -> {
                    if (Collections.class.isAssignableFrom(k.getJavaField().getType())) {
                        ReflectionUtils.setFieldCollectionValue(result, k.getJavaField(), (Collection) v);
                    } else {
                        ReflectionUtils.setFieldValue(result, k.getJavaField(), v);
                    }
                }
        );

        return result;
    }
}
