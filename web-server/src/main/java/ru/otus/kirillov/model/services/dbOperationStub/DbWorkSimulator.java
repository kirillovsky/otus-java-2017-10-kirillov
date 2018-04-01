package ru.otus.kirillov.model.services.dbOperationStub;

import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static java.util.Collections.emptyList;

public class DbWorkSimulator {

    private final DBService dbService;

    private final Random random = new Random(Calendar.getInstance().getTimeInMillis());

    public DbWorkSimulator(DBService dbService) {
        this.dbService = dbService;
    }

    public void randomUpdate() {
        List<UserDataSet> users = dbService.readAll(UserDataSet.class);
        long updateIndex = users.get(random.nextInt(users.size())).getId();
        UserDataSet user = dbService.read(updateIndex, UserDataSet.class);
        user = (user == null) ? new UserDataSet() : user;
        user.withName(getRandomName()).withAge(random.nextInt(100));
        dbService.saveOrUpdate(user);
    }

    public void randomSave() {
        dbService.saveOrUpdate(new UserDataSet(
                getRandomName(), random.nextInt(100), null, emptyList()
        ));
    }

    public void randomRead() {
        int usersSize = dbService.readAll(UserDataSet.class).size();
        dbService.read(random.nextInt((int) (usersSize * 1.75)), UserDataSet.class);
    }

    private String getRandomName() {
        return "NAME - " + LocalTime.now();
    }

    public void initDbEntities() {
        int entitiesSize = random.nextInt(99) + 1;
        for (int i = 0; i < entitiesSize; i++) {
            randomSave();
        }
    }


}
