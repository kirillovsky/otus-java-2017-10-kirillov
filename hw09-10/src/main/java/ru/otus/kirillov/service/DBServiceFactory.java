package ru.otus.kirillov.service;

import ru.otus.kirillov.configuration.DBServiceConfig;

/** Фабрика по созданию экземпляров {@link DBService}
 * Created by Александр on 24.01.2018.
 */
public interface DBServiceFactory {

    DBService createDBService(DBServiceConfig config);
}
