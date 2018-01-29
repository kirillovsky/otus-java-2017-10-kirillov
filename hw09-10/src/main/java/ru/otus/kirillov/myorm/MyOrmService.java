package ru.otus.kirillov.myorm;

import ru.otus.kirillov.model.DataSet;

import java.util.List;

/** Реализация ORM-фреймворка, имеющая ряд ограничений:
 * 1. Не проводится генераця id при вставке новой строчки в таблицу
 *
 * Created by Александр on 29.01.2018.
 */
public interface MyOrmService {

    <T extends DataSet> void saveOrUpdate(T t);

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> List<T> loadAll(Class<T> clazz);

    <T extends DataSet> void delete(T t);

    void destroy();
}
