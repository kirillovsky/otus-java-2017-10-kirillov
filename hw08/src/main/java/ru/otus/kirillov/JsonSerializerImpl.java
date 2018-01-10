package ru.otus.kirillov;

import java.io.IOException;
import java.io.OutputStream;

/** @see JsonSerializer
 * Позволяет сериализовывать следующие типы данных:
 * 1. Примитивные типы и строки
 * 2. Массиы примитивных типов, массивы объектов и многомерные массивы
 * 3. Enum-типы
 * 4. Объекты, учитывая иерархию наследования соответствующего класса
 * Содержимое полей {@link Object} не сериализуется.
 * Сериализуются все поля (объектов и примитивных типов), кроме отмеченных как transient
 * и синтетических полей. Кроме того, не сериализуются объекты внутренних классов объектов.
 * 5. Наследники классов {@link java.util.Collection} и {@link java.util.Map}
 * Created by Александр on 08.01.2018.
 */
public class JsonSerializerImpl implements JsonSerializer{


    @Override
    public String toJson(Object src) {
        return null;
    }

    @Override
    public void toJson(Object src, OutputStream outputStream) throws IOException {

    }
}
