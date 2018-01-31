package ru.otus.kirillov.myorm.commands.generatesql;

import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;
import ru.otus.kirillov.myorm.commands.generatesql.generators.DeleteQueryGenerator;
import ru.otus.kirillov.myorm.commands.generatesql.generators.InsertQueryGenerator;
import ru.otus.kirillov.myorm.commands.generatesql.generators.SelectQueryGenerator;
import ru.otus.kirillov.myorm.commands.generatesql.generators.UpdateQueryGenerator;

/**
 * Created by Александр on 30.01.2018.
 */
public class GenerateSqlCommand extends AbstractCommand<GenerateSQLRequest, String> {

    public GenerateSqlCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public String execute(GenerateSQLRequest request) {
        switch (request.getDmlType()) {
            case INSERT:
                return getInsertQuery(request);
            case UPDATE:
                return getUpdateQuery(request);
            case DELETE:
                return getDeleteQuery(request);
            case SELECT:
                return getSelectQuery(request);
            default:
                throw new RuntimeException("Unknown DML command type");
        }
    }

    private String getSelectQuery(GenerateSQLRequest request) {
        return new SelectQueryGenerator()
                .columns(request.getFieldsNames())
                .from(request.getTableName())
                .where(request.getWhereFieldsNames())
                .build();
    }

    private String getInsertQuery(GenerateSQLRequest request) {
        return new InsertQueryGenerator()
                .table(request.getTableName())
                .values(request.getFieldsNames())
                .build();
    }

    private String getUpdateQuery(GenerateSQLRequest request) {
        return new UpdateQueryGenerator()
                .table(request.getTableName())
                .set(request.getFieldsNames())
                .where(request.getWhereFieldsNames())
                .build();
    }

    private String getDeleteQuery(GenerateSQLRequest request) {
        return new DeleteQueryGenerator()
                .from(request.getTableName())
                .where(request.getWhereFieldsNames())
                .build();
    }
}
