package ru.otus.kirillov.myorm.executors;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.mapper.AbstractFieldDescriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Александр on 31.01.2018.
 */
public class DmlExecutor extends AbstractPreparedStatementExecutor {

    public DmlExecutor(Connection connection) {
        super(connection);
    }

    public void execute(String select, List<Pair<AbstractFieldDescriptor, Object>> columns) {
        try (PreparedStatement stmt = getConnection().prepareStatement(select)) {
            for (int i = 0; i < columns.size(); i++) {
                Pair<AbstractFieldDescriptor, Object> pair = columns.get(i);
                processPreparedStatement(stmt, i + 1, pair.getKey(), pair.getValue());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
