package ru.otus.kirillov.service.factory;

import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;
import ru.otus.kirillov.utils.ReflectionUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Александр on 29.01.2018.
 */
public abstract class AbstractDBServiceFactory implements DBServiceFactory {

    protected void registerDAO(DBService dbService, DBServiceConfig config) {
        getDaoClasses(config.getUsersDaoClasses())
                .forEach(clazz -> dbService.registerNewDao(clazz));
    }


    protected Stream<Class<?>> getDaoClasses(List<Class<?>> daoClasses) {
        return Stream.of(getDefaultDaoClasses(), daoClasses)
                .flatMap(List::stream);
    }

    protected Class<?> getDaoTypeArgument(Class<?> clazz) {
        Class<?> result = ReflectionUtils.getParentGenericType(clazz);

        if(result == null) {
            throw new RuntimeException("Cannot get actual argument type for class^ " + clazz.getName());
        }

        return result;
    }

    protected abstract List<Class<?>> getDefaultDaoClasses();


}
