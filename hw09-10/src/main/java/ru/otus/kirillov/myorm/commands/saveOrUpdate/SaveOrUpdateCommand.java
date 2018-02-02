package ru.otus.kirillov.myorm.commands.saveOrUpdate;

import ru.otus.kirillov.myorm.commands.AbstractCommand;
import ru.otus.kirillov.myorm.commands.CommandInvoker;

/** Алгоритм.
 * Created by Александр on 01.02.2018.
 */
public class SaveOrUpdateCommand extends AbstractCommand<SaveOrUpdateRequest, Void> {

    public SaveOrUpdateCommand(CommandInvoker invoker) {
        super(invoker);
    }

    @Override
    public Void execute(SaveOrUpdateRequest request) {
        return null;
    }
}
