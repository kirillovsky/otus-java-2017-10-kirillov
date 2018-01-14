package ru.otus.kirillov;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import static ru.otus.kirillov.utils.CommonUtils.*;

/**
 * @see JsonSerializer
 * Created by Александр on 08.01.2018.
 */
public class JsonSerializerImpl implements JsonSerializer {

    private static final Logger LOGGER = Logger.getLogger(getClassName(JsonSerializerImpl.class));

    @Override
    public String toJson(Object src) {
        if (src == null) return null;

        OutputStream strOutputStream = new ByteArrayOutputStream();
        try {
            toJson(src, strOutputStream);
        } catch (IOException e) {
            LOGGER.throwing(getClassName(), getMethodName(), e);
            throw new RuntimeException(e);
        }
        return src.toString();
    }

    @Override
    public void toJson(Object src, OutputStream outputStream) throws IOException {

    }
}
