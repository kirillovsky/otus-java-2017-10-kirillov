package ru.otus.kirillov.myorm;

import ru.otus.kirillov.model.DataSet;

import java.util.List;

/** Реализация ORM-фреймворка, имеющая ряд ограничений:
 * 1. Не проводится генераця id при вставке новой строчки в таблицу. Считаем, что он уже есть
 * 2. OneToMany - поле может иметь тип или Collection, или Set, или List (из-за сложностей инжектирования результата
 * - в джаве конструкторы не виртуальны).
 * Вроде как в Hibernate и Spring подобные проблемы решаются через DynamicProxy или GCLIB
 * 4. Ну разумеется, как и в Хибернейт, Entity должен иметь дефолтный открытый конструктор
 * 5. Не генирируется - скрипты создания Базы Данных
 * 6. {@link MyOrmServiceImpl} - выбран не очень удачный дизайн, поэтому местами код запутанный
 * 7. Но зато, можно легко расшириться на удаление/чтение не только по id, но и другим полям
 * Created by Александр on 29.01.2018.
 */
public interface MyOrmService {

    <T extends DataSet> void saveOrUpdate(T t);

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> List<T> loadAll(Class<T> clazz);

    <T extends DataSet> void delete(T t);

    void destroy();
}
