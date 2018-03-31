package ru.otus.kirillov.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/**
 * Утилиты для работы с воркерами, в нашем случае пул воркеров - {@link java.util.concurrent.ExecutorService}
 */
public final class WorkerUtils {

    @FunctionalInterface
    public interface ThrowableRunner {
        void run() throws Throwable;
    }

    private static final Logger log = LogManager.getLogger();

    private WorkerUtils() {
    }

    private static void checkWorkersCount(int processorsCount) {
        if (processorsCount <= 0) {
            throw new IllegalArgumentException("Processors count must be more than zero, actual - " + processorsCount);
        }
    }

    public static void initWorkers(ExecutorService service, int workersCount, ThrowableRunner runner) {
        CommonUtils.requiredNotNull(service);
        checkWorkersCount(workersCount);
        IntStream.range(0, workersCount + 1).forEach(i ->
                service.execute(() -> {
                    while (true) {
                        try {
                            runner.run();
                        } catch (Throwable e) {
                            log.catching(e);
                        }
                    }
                })
        );
    }


}
