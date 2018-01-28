package ru.otus.kirillov.service.factory.myorm;

import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;

/** Фабрика для создания {@link DBServiceFactory} с реализацией через
 * MyORM
 * Created by Александр on 28.01.2018.
 */
public class MyOrmDBServiceFactory implements DBServiceFactory {

    @Override
    public DBService createDBService(DBServiceConfig config) {
        return null;
    }
}
