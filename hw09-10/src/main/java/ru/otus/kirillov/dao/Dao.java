package ru.otus.kirillov.dao;

import ru.otus.kirillov.model.DataSet;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/** Базовый интерфейс для всех DAO.
 * Created by Александр on 22.01.2018.
 */
public interface Dao<T extends DataSet> {

    /**
     * Сохранение или обновление объекта модели в БД
     * @param object - объект модели для сохранения
     */
    T saveOrUpdate(T object);

    /**
     * Получение объекта модели по id
     * @param id - ключ
     * @return
     */
    T read(long id);

    /**
     * Получение всех объектов модели из БД
     * @return
     */
    List<T> readAll();

    /**
     * Удаление объекта модели из БД
     * @param object - объект сущности
     */
    void delete(T object);

    default Class<T> getEntityType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
