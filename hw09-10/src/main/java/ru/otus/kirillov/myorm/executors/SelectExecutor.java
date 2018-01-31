package ru.otus.kirillov.myorm.executors;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.mapper.AbstractFieldDescriptor;

import java.sql.*;
import java.util.*;

/**
 * Created by Александр on 30.01.2018.
 */
public class SelectExecutor extends AbstractPreparedStatementExecutor {

    public static final Map<JDBCType, ResultSetHandler> GET_PARAMS_MAP;

    static {
        Map<JDBCType, ResultSetHandler> tmpMap = new HashMap<>();
        tmpMap.put(JDBCType.BIGINT, (rs, sqlFieldName) -> rs.getInt(sqlFieldName));
        tmpMap.put(JDBCType.VARCHAR, (rs, sqlFieldName) -> rs.getString(sqlFieldName));

        GET_PARAMS_MAP = Collections.unmodifiableMap(tmpMap);
    }

    public SelectExecutor(Connection connection) {
        super(connection);
    }

    public List<Map<AbstractFieldDescriptor, Object>> execute(String select,
                                                              List<AbstractFieldDescriptor> selectedColumns,
                                                              List<Pair<AbstractFieldDescriptor, Object>> whereColumns) {

        List<Map<AbstractFieldDescriptor, Object>> result = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(select)) {

            for (int i = 0; i < whereColumns.size(); i++) {
                Pair<AbstractFieldDescriptor, Object> pair = whereColumns.get(i);
                processPreparedStatement(stmt, i + 1, pair.getKey(), pair.getValue());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<AbstractFieldDescriptor, Object> selectResult = new HashMap<>();
                    selectedColumns.forEach(column -> selectResult.put(column, processResultSet(rs, column)));
                    result.add(selectResult);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Object processResultSet(ResultSet rs, AbstractFieldDescriptor fieldDescriptor) {
        ResultSetHandler handler = GET_PARAMS_MAP.get(fieldDescriptor.getType());

        if (handler == null) {
            throw new IllegalStateException(
                    "Not found result set processor for field %s" + fieldDescriptor.getType()
            );
        }

        try {
            return handler.accept(rs, fieldDescriptor.getSqlFieldName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
