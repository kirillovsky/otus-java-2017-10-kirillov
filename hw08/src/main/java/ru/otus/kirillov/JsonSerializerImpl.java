package ru.otus.kirillov;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @see JsonSerializer
 * Created by Александр on 08.01.2018.
 */
//TODO: Добавить логгирование, через Log4j
public class JsonSerializerImpl implements JsonSerializer {


    @Override
    public String toJson(Object src) {
        if (src == null) return null;

        OutputStream strOutputStream = new ByteArrayOutputStream();
        try {
            toJson(src, strOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return src.toString();
    }

    @Override
    public void toJson(Object src, OutputStream outputStream) throws IOException {

    }
}
