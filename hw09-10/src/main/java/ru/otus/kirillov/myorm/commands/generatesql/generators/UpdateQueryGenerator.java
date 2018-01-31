package ru.otus.kirillov.myorm.commands.generatesql.generators;

import java.util.Collection;

/**
 * Created by Александр on 30.01.2018.
 */
public class UpdateQueryGenerator extends AbstractQueryGenerator {

    protected String taleName;

    public UpdateQueryGenerator() {
        super(GeneratorType.UPDATE);
    }

    public UpdateQueryGenerator table(String tableName) {
        this.taleName = tableName;
        return this;
    }

    public UpdateQueryGenerator set(Collection<String> fieldsName) {
        builder.append(taleName).append(" set ")
                .append(fieldsName.stream()
                        .map(str -> str + " = ?")
                        .collect(FIELD_DELIMETER_COLLECTOR)
                );
        return this;
    }

    public AbstractQueryGenerator where(Collection<String> fieldsName) {
        if (fieldsName.isEmpty()) {
            return this;
        }
        builder.append("where ").append(
                fieldsName.stream().map(s -> s + " = ?").collect(FIELD_DELIMETER_COLLECTOR)
        );
        return this;
    }
}
