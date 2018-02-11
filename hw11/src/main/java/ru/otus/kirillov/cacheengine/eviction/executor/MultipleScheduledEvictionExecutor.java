package ru.otus.kirillov.cacheengine.eviction.executor;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.eviction.commands.EvictionCommand;
import ru.otus.kirillov.cacheengine.utils.CommonUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/** Прогон комманд по расписанию. Метод execute вызвать можно только, если нужно прям сейчас
 * вызвать комманду.
 * Created by Александр on 08.02.2018.
 */
public class MultipleScheduledEvictionExecutor implements ScheduledCommandExecutor {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Timer timer = new Timer();
    private final List<Pair<EvictionCommand, Duration>> scheduledCommands = new ArrayList<>();

    public MultipleScheduledEvictionExecutor(List<Pair<EvictionCommand, Duration>> scheduledCommands) {
        CommonUtils.requiredNotNull(scheduledCommands);
        this.scheduledCommands.addAll(scheduledCommands);
    }

    @Override
    public void execute() {
        scheduledCommands.stream()
                .map(Pair::getLeft)
                .forEach(this::executeSafety);
    }

    // TODO: 08.02.2018 А вот тут очень интересный момент. Нельзя впихнуть лямбду в реализацию абстрактного класса
    // TODO: 08.02.2018 Интересно почему. Я же это делал. Из-за protected, а не public конструктора в нем?
    @Override
    public void start() {
        scheduledCommands.stream()
                .forEach(p -> timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                executeSafety(p.getLeft());
                            }
                        }, p.getRight().toMillis(), p.getRight().toMillis())
                );
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    private void executeSafety(EvictionCommand command) {
        try {
            command.execute();
        } catch (Exception e) {
            LOGGER.catching(e);
        }
    }
}
