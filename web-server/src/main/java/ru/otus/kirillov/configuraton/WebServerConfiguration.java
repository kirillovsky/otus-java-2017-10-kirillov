package ru.otus.kirillov.configuraton;

import org.jetbrains.annotations.NotNull;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.controller.*;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsCommand;
import ru.otus.kirillov.model.commands.login.LoginCommand;
import ru.otus.kirillov.model.commands.logout.LogoutCommand;
import ru.otus.kirillov.model.service.dbOperationStub.DbWorkScheduledSimulator;
import ru.otus.kirillov.model.service.dbOperationStub.DbWorkSimulator;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.model.service.auth.StubAuthServiceImpl;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.view.TemplateEngine;

import java.util.Arrays;

public final class WebServerConfiguration {

    private static final String ENCRYPTION_KEY = "MZygpewJsCpRrfOr";

    private WebServerConfiguration() {
    }

    static class AESSecurityInstance {
        private static AESSecurity security = new AESSecurity(ENCRYPTION_KEY);
    }

    static class AuthServiceInstance {
        private static AuthService authService = new StubAuthServiceImpl(AESSecurityInstance.security);
    }

    static class CacheEngineInstance {
        private static CacheEngine<String, DataSet> cacheEngine =
                new ConfigCacheEngineImplFactory(
                        new CacheEngineConfig()
                ).create();
    }

    static class DBServiceInstance {
        private static DBService cachedProxyDbService =
                new CachedProxyDBServiceFactory(
                        new HibernateDBServiceFactory(), CacheEngineInstance.cacheEngine
                ).createDBService(
                        new DBServiceConfig()
                                .withDbType(DBServiceConfig.DB.H2)
                                .withConnectionURL("jdbc:h2:mem:")
                );
    }

    static class CommandInvokerInstance {
        private static CommandInvoker commandInvoker =
                new CommandInvoker(AuthServiceInstance.authService,
                        Arrays.asList(
                                new LoginCommand(AuthServiceInstance.authService, AESSecurityInstance.security),
                                new LogoutCommand(AuthServiceInstance.authService),
                                new GetCacheStatsCommand(CacheEngineInstance.cacheEngine)
                        )
                );
    }

    static class TemplateEngineInstance {
        private static TemplateEngine templateEngine =
                new TemplateEngine();
    }

    @NotNull
    public static GetCacheStatsServlet createGetCacheStatsServlet() {
        return new GetCacheStatsServlet(
                TemplateEngineInstance.templateEngine,
                CommandInvokerInstance.commandInvoker
        );
    }

    @NotNull
    public static LoginServlet createLoginServlet() {
        return new LoginServlet(
                TemplateEngineInstance.templateEngine,
                CommandInvokerInstance.commandInvoker
        );
    }

    @NotNull
    public static ErrorPageServlet createErrorPageServlet() {
        return new ErrorPageServlet(
            TemplateEngineInstance.templateEngine
        );
    }

    @NotNull
    public static LogoutServlet createLogoutServlet() {
        return new LogoutServlet(
                TemplateEngineInstance.templateEngine,
                CommandInvokerInstance.commandInvoker
        );
    }

    @NotNull
    public static MainServlet createMainServlet() {
        return new MainServlet(
            TemplateEngineInstance.templateEngine
        );
    }

    @NotNull
    public static DbWorkScheduledSimulator createDbWorkSimulatorScheduler() {
        return new DbWorkScheduledSimulator(
                new DbWorkSimulator(DBServiceInstance.cachedProxyDbService)
        );
    }
}
