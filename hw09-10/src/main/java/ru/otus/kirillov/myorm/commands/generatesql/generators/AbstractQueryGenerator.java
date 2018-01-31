package ru.otus.kirillov.myorm.commands.generatesql.generators;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Александр on 30.01.2018.
 */
public class AbstractQueryGenerator {

    protected enum  GeneratorType {
        SELECT("select"),
        UPDATE("update"),
        INSERT("insert"),
        DELETE("delete");

        private String sqlOperationName;

        GeneratorType(String sqlOperationName) {
            this.sqlOperationName = sqlOperationName;
        }

        public String getSqlOperationName() {
            return sqlOperationName;
        }
    }

    protected static final Collector<CharSequence, ?, String> FIELD_DELIMETER_COLLECTOR =
            Collectors.joining(", ", "", " ");

    protected StringBuilder builder = new StringBuilder();

    public AbstractQueryGenerator(GeneratorType type) {
        builder.append(type.getSqlOperationName() + " ");
    }

    public String build() {
        return builder.toString();
    }
}
