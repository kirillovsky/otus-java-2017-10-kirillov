package ru.otus.kirillov.service.myorm;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceTest;
import ru.otus.kirillov.service.factory.myorm.MyOrmDBServiceFactory;
import ru.otus.kirillov.utils.H2ConnectionHelper;

import java.sql.Statement;

/**
 * Created by Александр on 28.01.2018.
 */
public class DBServiceMyOrmImplTest extends DBServiceTest {

    public static final String H2_TEST_DB_CONNECTION_STRING = "jdbc:h2:mem:test";

    protected static DBService dbService;

    @BeforeClass
    public static void init() throws Exception{
        createDBSchema();
        dbService = new MyOrmDBServiceFactory().createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL(H2_TEST_DB_CONNECTION_STRING)
        );
    }

    private static final String CREATE_ADDRESS_TABLE =
            "create table address (\n" +
                    "       id bigint generated by default as identity,\n" +
                    "        street varchar(255),\n" +
                    "        primary key (id)\n" +
                    "    )";

    private static final String CREATE_PHONE_TABLE =
            "create table phone (\n" +
                    "       id bigint generated by default as identity,\n" +
                    "        phone varchar(255),\n" +
                    "        phone_id bigint,\n" +
                    "        primary key (id)\n" +
                    "    )";

    private static final String CREATE_USER_TABLE =
            "create table user (\n" +
                    "       id bigint generated by default as identity,\n" +
                    "        age integer,\n" +
                    "        name varchar(255),\n" +
                    "        address_id bigint,\n" +
                    "        primary key (id)\n" +
                    "    )";

    public static void createDBSchema() throws Exception {
        Statement statement = H2ConnectionHelper.getConnection(H2_TEST_DB_CONNECTION_STRING)
                .createStatement();
        statement.addBatch(CREATE_PHONE_TABLE);
        statement.addBatch(CREATE_ADDRESS_TABLE);
        statement.addBatch(CREATE_USER_TABLE);
        statement.executeBatch();
    }

    @AfterClass
    public static void destroy() {
        ((DBServiceMyOrmImpl)dbService).destroy();
    }


    @After
    public void clearDB() {
        super.clearDB();
    }

    @Override
    protected DBService getDbService() {
        return dbService;
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
