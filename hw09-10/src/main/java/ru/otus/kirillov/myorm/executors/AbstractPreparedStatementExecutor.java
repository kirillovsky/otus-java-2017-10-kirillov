package ru.otus.kirillov.myorm.executors;

import ru.otus.kirillov.myorm.shema.elements.AbstractFieldDescriptor;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Александр on 30.01.2018.
 */
public abstract class AbstractPreparedStatementExecutor {

    public interface PreparedParamsSetter {
        void accept(PreparedStatement ps, Object o, int index) throws Exception;
    }

    public interface ResultSetHandler {
        Object accept(ResultSet rs, String sqlFieldName) throws Exception;
    }


    public static final Map<JDBCType, PreparedParamsSetter> SET_PARAMS_MAP;

    static {
        Map<JDBCType, PreparedParamsSetter> tmpMap = new HashMap<>();
        tmpMap.put(JDBCType.BIGINT, (ps, o, i) -> ps.setInt(i, (Integer)o));
        tmpMap.put(JDBCType.VARCHAR, (ps, o, i) -> ps.setString(i, (String)o));

        SET_PARAMS_MAP = Collections.unmodifiableMap(tmpMap);
    }

    private Connection connection;

    public AbstractPreparedStatementExecutor(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    protected void processPreparedStatement(PreparedStatement ps, int index, AbstractFieldDescriptor fieldDescriptor, Object o) {
        PreparedParamsSetter setter = SET_PARAMS_MAP.get(fieldDescriptor.getType());

        if (setter == null) {
            throw new IllegalStateException(
                    "Not found prepared statement processor for field type: " + fieldDescriptor.getType()
            );
        }

        try {
            setter.accept(ps, o, index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
