package ru.otus.kirillov;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.kirillov.model.commands.CommandInvokerTest;
import ru.otus.kirillov.model.services.dbOperationStub.DbWorkSimulatorTest;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsCommandTest;
import ru.otus.kirillov.model.commands.login.LoginCommandTest;
import ru.otus.kirillov.model.commands.logout.LogoutCommandTest;

import static org.junit.runners.Suite.SuiteClasses;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@SuiteClasses({
        GetCacheStatsCommandTest.class,
        LoginCommandTest.class,
        LogoutCommandTest.class,
        CommandInvokerTest.class,
        DbWorkSimulatorTest.class
})
public class AllTestRunner {
}
