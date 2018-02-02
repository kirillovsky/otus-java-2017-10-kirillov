package ru.otus.kirillov.myorm.executors;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.shema.elements.AbstractFieldDescriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Александр on 31.01.2018.
 */
public class DmlExecutor extends AbstractPreparedStatementExecutor {

    public DmlExecutor(Connection connection) {
        super(connection);
    }

    public void execute(String dmlQuery, List<Pair<AbstractFieldDescriptor, Object>> columns) {
        try (PreparedStatement stmt = getConnection().prepareStatement(dmlQuery)) {
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
