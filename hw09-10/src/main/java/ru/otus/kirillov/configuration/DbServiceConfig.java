package ru.otus.kirillov.configuration;

import ru.otus.kirillov.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.kirillov.utils.CommonUtils.retunIfNotNull;

/** Конфиг для создания {@link ru.otus.kirillov.service.DBService}
 * Created by Александр on 22.01.2018.
 */
public class DBServiceConfig {

    public enum DB {
        H2
    }

    private DB dbType;
    private String connectionURL;
    private String username = "";
    private String password = "";
    private List<Class<?>> usersDaoClasses = new ArrayList<>();

    public List<Class<?>> getUsersDaoClasses() {
        return usersDaoClasses;
    }

    public DBServiceConfig withNewUsersDaoClasses(Class<? extends Dao> usersDaoClass) {
        this.usersDaoClasses.add(retunIfNotNull(usersDaoClass));
        return this;
    }

    public DB getDbType() {
        return retunIfNotNull(dbType);
    }

    public DBServiceConfig withDbType(DB dbType) {
        this.dbType = retunIfNotNull(dbType);
        return this;
    }

    public String getConnectionURL() {
        return retunIfNotNull(connectionURL);
    }

    public DBServiceConfig withConnectionURL(String connectionURL) {
        this.connectionURL = retunIfNotNull(connectionURL);
        return this;
    }

    public String getUsername() {
        return username == null ? "": username;
    }

    public DBServiceConfig withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password == null ? "": password;
    }

    public DBServiceConfig withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConnectionString() {
        return connectionURL
                + (username.isEmpty() ? "":
                "?user=" + username + "&password=" + (username.isEmpty() ? "": password));
    }
}
