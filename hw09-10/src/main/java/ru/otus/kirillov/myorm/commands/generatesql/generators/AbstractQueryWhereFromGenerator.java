package ru.otus.kirillov.myorm.commands.generatesql.generators;

import java.util.Collection;

/**
 * Created by Александр on 30.01.2018.
 */
public class AbstractQueryWhereFromGenerator extends AbstractQueryGenerator {

    public AbstractQueryWhereFromGenerator(GeneratorType type) {
        super(type);
    }


    public AbstractQueryWhereFromGenerator where(Collection<String> fieldsName) {
        if (fieldsName.isEmpty()) {
            return this;
        }
        builder.append("where ").append(
                fieldsName.stream().map(s -> s + " = ?").collect(FIELD_DELIMETER_COLLECTOR)
        );
        return this;
    }


    public AbstractQueryWhereFromGenerator from(String tableName) {
        builder.append("from " + tableName);
        return this;
    }
}
