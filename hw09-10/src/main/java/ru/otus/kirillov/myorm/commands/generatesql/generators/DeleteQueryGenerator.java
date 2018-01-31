package ru.otus.kirillov.myorm.commands.generatesql.generators;

/**
 * Created by Александр on 30.01.2018.
 */
public class DeleteQueryGenerator extends AbstractQueryWhereFromGenerator {

    public DeleteQueryGenerator() {
        super(GeneratorType.DELETE);
    }
}
