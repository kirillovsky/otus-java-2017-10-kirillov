package ru.otus.kirillov.atm.commands.invokers;

import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.commands.*;
import ru.otus.kirillov.atm.commands.queries.Query;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Обработчик комманд.
 * Created by Александр on 08.12.2017.
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Command DEFAULT_COMMAND = new UnknownCommand();
    private Map<Query.Type, Command> typeCommandMap = new HashMap<>();
    private CellManagement cellManagement;

    public CommandInvokerImpl(CellManagement cellManagement) {
        setCellManagement(cellManagement);
        typeCommandMap.put(Query.Type.BALANCE, new BalanceCommand(cellManagement));
        typeCommandMap.put(Query.Type.DEPOSITING, new DepositCommand(cellManagement));
        typeCommandMap.put(Query.Type.WITHRDAWAL, new WithdrawCommand(cellManagement));
        typeCommandMap.put(Query.Type.UNDO_TO_INITIAL, new UndoToDefaultCommand(cellManagement));
    }

    public CommandInvokerImpl(CellManagement cellManagement,
                              Map<Query.Type, Function<CellManagement, ? extends Command>> creatorsCommandMap) {
        setCellManagement(cellManagement);
        Commons.requiredMapValuesNotNull(creatorsCommandMap);
        creatorsCommandMap.forEach((k, v) -> typeCommandMap.put(k, v.apply(cellManagement)));
    }

    public void setCellManagement(CellManagement cellManagement) {
        Commons.requiredNotNull(cellManagement, "cell management must be not null");
        this.cellManagement = cellManagement;
    }

    @Override
    public <RQ extends Query, RS> RS process(RQ request) {
        Command<RQ, RS> command = typeCommandMap.getOrDefault(request.getType(), DEFAULT_COMMAND);
        return command.execute(request);
    }

    @Override
    public <RQ extends Query, RS> void addCommand(Query.Type type, Command<RQ, RS> command) {
        typeCommandMap.put(type, command);
    }
}
