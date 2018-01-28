package ru.otus.kirillov.service.myorm;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceTest;
import ru.otus.kirillov.service.factory.myorm.MyOrmDBServiceFactory;

/**
 * Created by Александр on 28.01.2018.
 */
public class DBServiceMyOrmImplTest extends DBServiceTest {

    protected static DBService dbService;

    @BeforeClass
    public static void init() {
        dbService = new MyOrmDBServiceFactory().createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:test")
        );
    }

    @Override
    protected DBService getDbService() {
        return dbService;
    }

    @After
    public void clearDB() {
        super.clearDB();
    }

    @Test
    public void emptyUserReadTest() {
        super.emptyUserReadTest();
    }

    @Test
    public void readOneSimpleUser() {
        super.readOneSimpleUser();
    }

    @Test
    public void readOneFillUser() {
        super.readOneFillUser();
    }

    @Test
    public void readOneFillUserSeparatelySave() {
        super.readOneFillUserSeparatelySave();
    }

    @Test
    public void updateSimpleFields() {
        super.updateSimpleFields();
    }

    @Test
    public void updateDependentDataSets() {
        super.updateDependentDataSets();
    }

    @Test
    public void updateExtendDependentDataSets() {
        super.updateExtendDependentDataSets();
    }

    @Test
    public void updateReduceDependentDataSets() {
        super.updateReduceDependentDataSets();
    }

    @Test
    public void deleteSimpleDataSets() {
        super.deleteSimpleDataSets();
    }

    @Test
    public void deleteUserWithoutDependentEntities() {
        super.deleteUserWithoutDependentEntities();
    }
}
