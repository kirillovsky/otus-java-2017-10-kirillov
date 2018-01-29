package ru.otus.kirillov.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionHelper {

    public static Connection getConnection(String connectionString) {
        try {
            DriverManager.registerDriver(new org.h2.Driver());

            String url = connectionString;
            Connection result = DriverManager.getConnection(url);
            result.setAutoCommit(false);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
