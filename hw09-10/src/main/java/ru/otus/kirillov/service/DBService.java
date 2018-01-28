package ru.otus.kirillov.service;

import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;

import java.util.List;

/** Интерфейс CRUD-операций в БД
 * Created by Александр on 20.01.2018.
 */
public interface DBService {

    /**
     * Сохранение или обновление объекта модели в БД
     * @param object - объект модели для сохранения
     * @param <T> - тип модели
     */
    <T extends DataSet> T saveOrUpdate(T object);

    /**
     * Получение объекта модели по id
     * @param id - ключ
     * @param clazz - класс сущности
     * @param <T> - тип модели
     * @return
     */
    <T extends DataSet> T read(long id, Class<T> clazz);

    /**
     * Получение всех объектов модели из БД
     * @param clazz - класс сущности
     * @param <T> - тип модели
     * @return
     */
    <T extends DataSet> List<T> readAll(Class<T> clazz);

    /**
     * Удаление объекта модели из БД
     * @param object - объект сущности
     * @param <T> - тип модели
     */
    <T extends DataSet> void delete(T object);

    /**Получение Dao по классу сущности.
     * В случае, если потребуется какая-то специфичная операция из DAO,
     * не добавленная в интерфейс {@link DBService}
     * @param entityClass - класс сущности
     * @param <T>
     * @return DAO-для данной сущности
     * @throws RuntimeException - если для данного класса не был зарегистрирован
     * ни один DAO
     */
    <T extends DataSet> Dao<T> getDao(Class<T> entityClass);

    /**Регистрация нового DAO.
     * Класс DAO должен иметь возможность сохранить в рантайме свой дженерик-тип!
     * @param <T>
     * @param daoClass - тип DAO
     */
    void registerNewDao(Class<?> daoClass);
}
