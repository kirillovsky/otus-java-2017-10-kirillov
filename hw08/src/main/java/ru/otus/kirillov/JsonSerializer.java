package ru.otus.kirillov;

import java.io.IOException;
import java.io.OutputStream;

/** Интерфейс сериализатора объектов в формат Json.
 * Позволяет сериализовывать следующие типы данных:
 * 1. Примитивные типы и строки
 * 2. Массиы примитивных типов, массивы объектов и многомерные массивы
 * 3. Enum-типы
 * 4. Объекты, учитывая иерархию наследования соответствующего класса
 * Поля со значением null, не сериализуются
 * Внутреннее содержимое объектов {@link Object} не сериализуется.
 * Сериализуются все поля (объектов и примитивных типов), кроме отмеченных как transient
 * и синтетических полей. Кроме того, не сериализуются объекты внутренних классов объектов.
 * 5. Наследники классов {@link java.util.Collection} и {@link java.util.Map}
 * Created by Александр on 08.01.2018.
 */
public interface JsonSerializer {

    /**
     * Сериализация объекта в Json-строку.
     * @param src - объект для сериализации
     * @return строка в формате Json
     */
    String toJson(Object src);

    /**
     * Сериализация объекта и запись его в поток
     * @param src - объект для сериализации
     * @param outputStream - поток для записи Json-представления объекта
     */
    void toJson(Object src, OutputStream outputStream) throws IOException;
}
