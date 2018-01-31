package ru.otus.kirillov.myorm.commands.generatesql.generators;

import java.util.Collection;

/**
 * Created by Александр on 30.01.2018.
 */
public class InsertQueryGenerator extends AbstractQueryGenerator {

    private String tableName;

    public InsertQueryGenerator() {
        super(GeneratorType.INSERT);
    }

    public InsertQueryGenerator values(Collection<String> values) {
        builder.append("into ")
                .append(tableName)
                .append(" (")
                .append(values.stream().collect(FIELD_DELIMETER_COLLECTOR))
                .append(") ")
                .append("values(")
                .append(values.stream().map(s -> "?").collect(FIELD_DELIMETER_COLLECTOR))
                .append(")");
        return this;
    }

    public InsertQueryGenerator table(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
