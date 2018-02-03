package ru.otus.kirillov.myorm.executors;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.schema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.utils.CommonUtils;

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
        CommonUtils.putPair(tmpMap, withNulls(JDBCType.BIGINT, (ps, o, i) -> ps.setLong(i, (Long) o)));
        CommonUtils.putPair(tmpMap, withNulls(JDBCType.INTEGER, (ps, o, i) -> ps.setInt(i, (Integer)o)));
        CommonUtils.putPair(tmpMap, withNulls(JDBCType.VARCHAR, (ps, o, i) -> ps.setString(i, (String)o)));

//        tmpMap.put(JDBCType.BIGINT, (ps, o, i) -> ps.setLong(i, (Long) o));
//        tmpMap.put(JDBCType.INTEGER, (ps, o, i) -> ps.setInt(i, (Integer)o));
//        tmpMap.put(JDBCType.VARCHAR, (ps, o, i) -> ps.setString(i, (String)o));

        SET_PARAMS_MAP = Collections.unmodifiableMap(tmpMap);
    }

    private static Pair<JDBCType, PreparedParamsSetter> withNulls(JDBCType type, PreparedParamsSetter notNullSetter) {
        return Pair.of(type, (PreparedParamsSetter)(ps, o, i) -> {
            if(o == null) {
                ps.setNull(i, type.getVendorTypeNumber());
            } else {
                notNullSetter.accept(ps, o, i);
            }
        });
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
