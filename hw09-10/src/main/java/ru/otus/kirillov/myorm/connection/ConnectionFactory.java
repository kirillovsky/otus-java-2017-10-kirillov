package ru.otus.kirillov.myorm.connection;

import java.sql.Connection;

/** Фабирка для создания коннекшенов
 * Created by Александр on 29.01.2018.
 */
public interface ConnectionFactory {

    Connection getConnection();

    void closeConnections();
}
