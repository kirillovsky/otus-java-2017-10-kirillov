package ru.otus.kirillov.myorm.connection;

import ru.otus.kirillov.utils.CommonUtils;
import ru.otus.kirillov.utils.H2ConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Александр on 29.01.2018.
 */
public class SingletonH2ConnectionFactory implements ConnectionFactory {

    private static Connection connection;
    private static String connectionString;

    public SingletonH2ConnectionFactory(String connectionString) {
        this.connectionString = CommonUtils.retunIfNotNull(connectionString);
    }

    static class ConnectionHolder {
        static Connection getConnection() {
            return H2ConnectionHelper.getConnection(SingletonH2ConnectionFactory.connectionString);
        }
    }

    @Override
    public Connection getConnection() {
        return connection == null ? ConnectionHolder.getConnection(): connection;
    }

    @Override
    public void closeConnections() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
