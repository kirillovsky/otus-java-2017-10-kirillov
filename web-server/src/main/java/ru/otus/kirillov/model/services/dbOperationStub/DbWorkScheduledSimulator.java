package ru.otus.kirillov.model.service.dbOperationStub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DbWorkScheduledSimulator {

    private static final long DELAY_TIME_IN_MS = 50;

    private static final Logger log = LogManager.getLogger();

    private final ScheduledExecutorService executorService =  Executors.newScheduledThreadPool(1);

    private final DbWorkSimulator dbWorkSimulator;

    public DbWorkScheduledSimulator(DbWorkSimulator dbWorkSimulator) {
        this.dbWorkSimulator = dbWorkSimulator;
    }

    public void start() {
        dbWorkSimulator.initDbEntities();
        executorService.scheduleWithFixedDelay(() -> {
            try {
                switch ((int) (getTimeMills() % 3)) {
                    case 0:
                        dbWorkSimulator.randomRead();
                        break;
                    case 1:
                        dbWorkSimulator.randomSave();
                        break;
                    default:
                        dbWorkSimulator.randomUpdate();
                }
            } catch (Exception e) {
                log.catching(e);
            }
        }, 0, DELAY_TIME_IN_MS, TimeUnit.MILLISECONDS);
    }

    private long getTimeMills() {
        return System.currentTimeMillis();
    }

    public void detach() {
        executorService.shutdownNow();
    }
}
