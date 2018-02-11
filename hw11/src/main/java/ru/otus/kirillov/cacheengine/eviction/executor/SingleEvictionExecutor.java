package ru.otus.kirillov.cacheengine.eviction.executor;

import ru.otus.kirillov.cacheengine.eviction.commands.EvictionCommand;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

/** Ручной исполнитель комманды.
 * Created by Александр on 08.02.2018.
 */
public class SingleEvictionExecutor implements EvictionExecutor {

    private final EvictionCommand command;

    public SingleEvictionExecutor(EvictionCommand command) {
        this.command = CommonUtils.returnIfNotNull(command);
    }

    @Override
    public void execute() {
        command.execute();
    }
}
