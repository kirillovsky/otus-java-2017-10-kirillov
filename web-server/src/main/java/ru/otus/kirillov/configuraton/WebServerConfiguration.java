package ru.otus.kirillov.configuraton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.config.CacheEngineConfig;
import ru.otus.kirillov.cacheengine.impl.ConfigCacheEngineImplFactory;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.channels.BlockingQueueChannel;
import ru.otus.kirillov.model.channels.Channel;
import ru.otus.kirillov.model.channels.DuplexChannel;
import ru.otus.kirillov.model.channels.SimpleDuplexChannel;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.emptyCommand.EmptyCommand;
import ru.otus.kirillov.model.commands.login.LoginCommand;
import ru.otus.kirillov.model.commands.logout.LogoutCommand;
import ru.otus.kirillov.model.commands.remote.RemoteOpsCommand;
import ru.otus.kirillov.model.services.auth.AuthService;
import ru.otus.kirillov.model.services.auth.StubAuthServiceImpl;
import ru.otus.kirillov.model.services.dbOperationStub.DbWorkScheduledSimulator;
import ru.otus.kirillov.model.services.dbOperationStub.DbWorkSimulator;
import ru.otus.kirillov.controllers.sockets.getCacheStats.GetCacheStatsSocketController;
import ru.otus.kirillov.model.services.remoteService.cache.RemoteCacheEngineService;
import ru.otus.kirillov.model.transport.AsyncMessageClient;
import ru.otus.kirillov.model.transport.Message;
import ru.otus.kirillov.model.transport.MessageClient;
import ru.otus.kirillov.model.transport.Requests.Request;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.factory.cache.CachedProxyDBServiceFactory;
import ru.otus.kirillov.service.factory.hibernate.HibernateDBServiceFactory;
import ru.otus.kirillov.utils.AESSecurity;
import ru.otus.kirillov.view.TemplateEngine;
import ru.otus.kirillov.view.View;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import static ru.otus.kirillov.model.transport.Requests.*;

@Configuration
public class WebServerConfiguration {

    private static final String ENCRYPTION_KEY = "MZygpewJsCpRrfOr";

    @Value("${frontend.cache-stats.port}")
    private String cacheStatPortNumber;

    @Value("${frontend.cache-stats.endpoint}")
    private String endpoint;

    @Value("${frontend.host}")
    private String host;

    @Value("${workersPool.size}")
    private String workersPoolSize;

    @Value("${cacheEngine.queue.workers.count}")
    private String cacheEngineQueueWorkersCount;

    @Value("${frontend.queue.workers.count}")
    private String frontendQueueWorkersCount;

    @Bean
    public AESSecurity security() {
        return new AESSecurity(ENCRYPTION_KEY);
    }

    @Bean
    public AuthService authService() {
        return new StubAuthServiceImpl(security());
    }

    @Bean
    public CacheEngine<String, DataSet> cacheEngine() {
        return new ConfigCacheEngineImplFactory(
                new CacheEngineConfig()
        ).create();
    }

    @Bean
    public DBService dbService() {
        return new CachedProxyDBServiceFactory(
                new HibernateDBServiceFactory(), cacheEngine()
        ).createDBService(
                new DBServiceConfig()
                        .withDbType(DBServiceConfig.DB.H2)
                        .withConnectionURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1")
        );
    }

    @Bean
    public CommandInvoker invoker() {
        return new CommandInvoker(authService(),
                Arrays.asList(
                        new LoginCommand(authService(), security()),
                        new LogoutCommand(authService()),
                        new RemoteOpsCommand(frontendAsyncClient()),
                        new EmptyCommand()
                )
        );
    }

    @Bean
    public TemplateEngine templateEngine() {
        return new TemplateEngine(Arrays.asList(View.values()));
    }

    @Bean(initMethod = "start", destroyMethod = "detach")
    public DbWorkScheduledSimulator dbWorkSimulator() {
        return new DbWorkScheduledSimulator(
                new DbWorkSimulator(dbService())
        );
    }

    @Bean(initMethod = "start", destroyMethod = "detach")
    public GetCacheStatsSocketController cacheStatsService() {
        return new GetCacheStatsSocketController(host, endpoint, cacheStatPortNumber, invoker());
    }

    @Bean
    public RemoteCacheEngineService remoteCacheEngineService() {
        return new RemoteCacheEngineService(cacheEngine(), cacheEngineClient());
    }

    @Bean
    public AsyncMessageClient<Request, Response> frontendAsyncClient() {
        Map<Class<? extends Message>, DuplexChannel<Request, Response>> channelMap = new HashMap<>();
        channelMap.put(RemoteCacheOperation.class, frontendDuplexChannel());
        return new AsyncMessageClient<>(channelMap);
    }

    @Bean
    public MessageClient<Response, Request> cacheEngineClient() {
        return new MessageClient<>(cacheEngineDuplexChannel());
    }

    @Bean
    public DuplexChannel<Request, Response> frontendDuplexChannel() {
        return new SimpleDuplexChannel<>(
                frontendToCacheEngineChannel(),
                cacheEngineToFrontendChannel()
        );
    }

    @Bean
    public DuplexChannel<Response, Request> cacheEngineDuplexChannel() {
        return new SimpleDuplexChannel<>(
                cacheEngineToFrontendChannel(),
                frontendToCacheEngineChannel()
        );
    }

    @Bean
    public Channel<Request> frontendToCacheEngineChannel() {
        return new BlockingQueueChannel<>(
                frontendToCacheEngineQueue(),
                workersPool(),
                Integer.parseInt(cacheEngineQueueWorkersCount)
        );
    }

    @Bean
    public Channel<Response> cacheEngineToFrontendChannel() {
        return new BlockingQueueChannel<>(
                cacheEngineToFrontendQueue(),
                workersPool(),
                Integer.parseInt(frontendQueueWorkersCount)
        );
    }

    @Bean
    public BlockingQueue<Request> frontendToCacheEngineQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Response> cacheEngineToFrontendQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public ExecutorService workersPool() {
        return Executors.newFixedThreadPool(Integer.parseInt(workersPoolSize));
    }
}
