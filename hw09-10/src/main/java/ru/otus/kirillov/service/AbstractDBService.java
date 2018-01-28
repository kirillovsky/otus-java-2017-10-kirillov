package ru.otus.kirillov.service;

import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Абстрактная промежуточная реализация {@link DBService}
 * Created by Александр on 23.01.2018.
 */
public abstract class AbstractDBService implements DBService {

    protected Map<Class<? extends DataSet>, Dao> daoByEntityClassMap = new HashMap<>();

    @Override
    public <T extends DataSet> T saveOrUpdate(T object) {
        if (object == null)
            return null;
        return getDao((Class<T>) object.getClass()).saveOrUpdate(object);
    }

    @Override
    public <T extends DataSet> T read(long id, Class<T> clazz) {
        CommonUtils.requiredNotNull(clazz);
        return getDao(clazz).read(id);
    }

    @Override
    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        CommonUtils.requiredNotNull(clazz);
        return getDao(clazz).readAll();
    }

    @Override
    public <T extends DataSet> void delete(T object) {
        if (object == null)
            return;
        getDao((Class<T>) object.getClass()).delete(object);
    }

    // TODO: 23.01.2018 Можно добавить дефолтный DAO, для ранее не зарегистрироанных сущностей. Но пока не понятно насколько это нужно
    @Override
    public <T extends DataSet> Dao<T> getDao(Class<T> entityClass) {
        CommonUtils.requiredNotNull(entityClass);
        if (!daoByEntityClassMap.containsKey(entityClass)) {
            throw new RuntimeException("DAO for class %s is not registered");
        }
        return daoByEntityClassMap.get(entityClass);
    }

    @Override
    public void registerNewDao(Class<?> daoClass) {
        Dao<?> newDao = createDao(daoClass);
        daoByEntityClassMap.put(newDao.getEntityType(), newDao);
    }

    /**
     * Этот метод отвечает за создание нового DAO по его классу.
     * В зависимости от конкретной реалзиации {@link DBService}
     * для создание DAO могут потребоваться разные данные
     *
     * @param daoClass
     * @return
     */
    protected abstract Dao<?> createDao(Class<?> daoClass);
}
