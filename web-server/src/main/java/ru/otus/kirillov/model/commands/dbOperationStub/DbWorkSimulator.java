package ru.otus.kirillov.model.commands.dbOperationStub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;

public class DbWorkSimulator {

    public static final long DELAY_TIME_IN_MS = 50;
    private static final Logger log = LogManager.getLogger();

    private final DBService dbService;
    private final ScheduledExecutorService executorService;
    private final Random random = new Random(Calendar.getInstance().getTimeInMillis());

    public DbWorkSimulator(DBService dbService) {
        this.dbService = dbService;
        initDbEntities();
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() -> {
            try {
                switch (random.nextInt(3)) {
                    case 0:
                        randomRead();
                        break;
                    case 1:
                        randomSave();
                        break;
                    default:
                        randomUpdate();
                }
            } catch (Exception e) {
                log.catching(e);
            }
        }, 0, DELAY_TIME_IN_MS, TimeUnit.MILLISECONDS);
    }

    public void detach() {
        executorService.shutdownNow();
    }

    private void randomUpdate() {
        int usersSize = dbService.readAll(UserDataSet.class).size();
        int updateIndex = random.nextInt(usersSize);
        UserDataSet user = dbService.read(updateIndex, UserDataSet.class);
        user = (user == null) ? new UserDataSet(): user;
        user.withName(getRandomName()).withAge(random.nextInt(100));
        dbService.saveOrUpdate(user);
    }

    private void randomSave() {
        dbService.saveOrUpdate(new UserDataSet(
                getRandomName(), random.nextInt(100), null, emptyList()
        ));
    }

    private void randomRead() {
        int usersSize = dbService.readAll(UserDataSet.class).size();
        dbService.read(random.nextInt((int) (usersSize * 1.75)), UserDataSet.class);
    }

    private String getRandomName() {
        return "NAME - " + LocalTime.now();
    }

    private void initDbEntities() {
        int entitiesSize = random.nextInt(99) + 1;
        for (int i = 0; i < entitiesSize; i++) {
            randomSave();
        }
    }


}
