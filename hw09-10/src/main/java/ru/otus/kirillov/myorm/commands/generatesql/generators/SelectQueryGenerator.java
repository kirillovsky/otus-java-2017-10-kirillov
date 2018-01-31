package ru.otus.kirillov.myorm.commands.generatesql.generators;

import java.util.Collection;

/**
 * Created by Александр on 30.01.2018.
 */
public class SelectQueryGenerator extends AbstractQueryWhereFromGenerator {

    private StringBuilder builder = new StringBuilder();

    public SelectQueryGenerator() {
        super(GeneratorType.SELECT);
    }

    public SelectQueryGenerator columns(Collection<String> fieldsName) {
        builder.append("select ").append(
                fieldsName.stream().collect(FIELD_DELIMETER_COLLECTOR)
        );
        return this;
    }
}
