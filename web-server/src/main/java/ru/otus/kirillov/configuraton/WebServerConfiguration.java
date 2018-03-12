package ru.otus.kirillov.configuraton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsCommand;
import ru.otus.kirillov.model.commands.login.LoginCommand;
import ru.otus.kirillov.model.commands.logout.LogoutCommand;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.model.service.auth.StubAuthServiceImpl;
import ru.otus.kirillov.model.service.dbOperationStub.DbWorkSimulator;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.view.TemplateEngine;
import ru.otus.kirillov.view.View;

import java.util.Arrays;

@Configuration
public class WebServerConfiguration {

    private static final String ENCRYPTION_KEY = "MZygpewJsCpRrfOr";

    @Bean
    @Scope("singleton")
    public AESSecurity security() {
        return new AESSecurity(ENCRYPTION_KEY);
    }

    @Bean
    @Scope("singleton")
    public AuthService authService() {
        return new StubAuthServiceImpl(security());
    }

    @Bean
    @Scope("singleton")
    public CacheEngine<String, DataSet> cacheEngine() {
        return new ConfigCacheEngineImplFactory(
                new CacheEngineConfig()
        ).create();
    }

    @Bean
    @Scope("singleton")
    public DBService dbService() {
        return new CachedProxyDBServiceFactory(
                new HibernateDBServiceFactory(), cacheEngine()
        ).createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:")
        );
    }

    @Bean
    @Scope("singleton")
    public CommandInvoker invoker() {
        return new CommandInvoker(authService(),
                Arrays.asList(
                        new LoginCommand(authService(), security()),
                        new LogoutCommand(authService()),
                        new GetCacheStatsCommand(cacheEngine())
                )
        );
    }

    @Bean
    @Scope("singleton")
    public TemplateEngine templateEngine() {
        return new TemplateEngine(Arrays.asList(View.values()));
    }


    @Bean
    @Scope("singleton")
    public DbWorkSimulator dbWorkSimulator() {
        return new DbWorkSimulator(dbService());
    }
}
