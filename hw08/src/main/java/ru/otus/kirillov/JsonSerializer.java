package ru.otus.kirillov;

import java.io.IOException;
import java.io.OutputStream;

/** Интерфейс сериализатора объектов в формат Json.
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
