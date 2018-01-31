package ru.otus.kirillov.dao.myorm;

import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.MyOrmService;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.List;

/**
 * Created by Александр on 29.01.2018.
 */
public abstract class AbstractMyOrmDao<T extends DataSet> implements Dao<T> {

    private MyOrmService service;

    public void setService(MyOrmService service) {
        this.service = CommonUtils.retunIfNotNull(service);
    }

    @Override
    public T saveOrUpdate(T object) {
        service.saveOrUpdate(CommonUtils.retunIfNotNull(object));
        return object;
    }

    @Override
    public T read(long id) {
        T result = service.load(id, getEntityType());

        if(result == null) {
            throw new IllegalArgumentException(
                    String.format("Not found entity (%s) with id = %d", getEntityType().getName(), id)
            );
        }
        return result;
    }

    @Override
    public List<T> readAll() {
        return service.loadAll(getEntityType());
    }

    @Override
    public void delete(T object) {
        service.delete(object);
    }
}
