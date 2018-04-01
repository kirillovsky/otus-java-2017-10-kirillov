package ru.otus.kirillov.controllers.sockets.getCacheStats;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.controllers.sockets.getCacheStats.messages.GetCacheStatsUiErrorRs;
import ru.otus.kirillov.controllers.sockets.getCacheStats.messages.GetCacheStatsUiRq;
import ru.otus.kirillov.controllers.sockets.getCacheStats.messages.GetCacheStatsUiSuccessRs;
import ru.otus.kirillov.model.commands.CommandInvoker;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.common.SessionModelRequestWrapper;
import ru.otus.kirillov.model.commands.remote.RemoteOpsModelRequest;
import ru.otus.kirillov.model.commands.remote.RemoteOpsModelResult;
import ru.otus.kirillov.model.transport.Responses;
import ru.otus.kirillov.model.transport.Responses.ExceptionResponse;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.model.transport.Responses.ValueResponse;
import ru.otus.kirillov.utils.CommonUtils;
import static ru.otus.kirillov.model.transport.Requests.RemoteCacheOperation;
import static ru.otus.kirillov.model.transport.Requests.RemoteOperation;

public class GetCacheStatsSocketController {

    private static final Logger log = LogManager.getLogger();

    private static final String UI_INFO_EXCHANGE_EVENT_TYPE = "message";

    private final String hostName;
    private final String endpointName;
    private final int portName;
    private final CommandInvoker invoker;

    private SocketIOServer server;

    public GetCacheStatsSocketController(String hostName, String endpointName, String portName, CommandInvoker invoker) {
        this.hostName = CommonUtils.retunIfNotNull(hostName);
        this.endpointName = CommonUtils.retunIfNotNull(endpointName);
        this.portName = Integer.valueOf(CommonUtils.retunIfNotNull(portName));
        this.invoker = CommonUtils.retunIfNotNull(invoker);
    }

    public void start() {
        Configuration config = createConfig();
        server = createServer(config);
        server.start();
    }

    public void detach() {
        if (server != null) {
            server.stop();
        }
    }

    private Configuration createConfig() {
        Configuration config = new Configuration();
        config.setHostname(hostName);
        config.setPort(portName);
        return config;
    }

    private SocketIOServer createServer(Configuration config) {
        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(client -> log.info(String.format("Client - %s, connected", client)));
        SocketIONamespace endpoint = server.addNamespace(endpointName);
        endpoint.addEventListener(UI_INFO_EXCHANGE_EVENT_TYPE, GetCacheStatsUiRq.class, (client, data, ackSender) -> {
            log.debug("UI Message received: " + data);
            ModelResult rs = sendResponse(data);
            if (rs instanceof ErroneousModelResult) {
                log.error("Model error: " + data);
                sendError(client, ((ErroneousModelResult)rs).getCause());
            } else if (rs instanceof RemoteOpsModelResult) {
                @SuppressWarnings("unchecked")
                RemoteOpsModelResult<Response> actualRs = (RemoteOpsModelResult<Response>) rs;
                actualRs.getAsyncResult()
                        .thenApplyAsync(GetCacheStatsSocketController::getResponseValue)
                        .thenAcceptAsync(stats -> sendSuccess(client, stats))
                        .exceptionally(e -> sendError(client, e));
            }

        });
        return server;
    }

    private ModelResult sendResponse(GetCacheStatsUiRq rq) {
        RemoteOperation<CacheEngine, CacheStatistics> remoteOp = new RemoteCacheOperation<>(CacheEngine::getStats);
        RemoteOpsModelRequest<CacheEngine, CacheStatistics> remoteOpsModelRq = new RemoteOpsModelRequest<>(remoteOp);
        return invoker.execute(createSessionWrapper(rq, remoteOpsModelRq));
    }

    private static <S, R> SessionModelRequestWrapper<RemoteOpsModelRequest<S, R>>
    createSessionWrapper(GetCacheStatsUiRq rq, RemoteOpsModelRequest<S, R> remoteOp) {
        return SessionModelRequestWrapper.of(
                CommonUtils.retunIfNotNull(rq.getSessionId()),
                CommonUtils.retunIfNotNull(rq.getUserName()),
                remoteOp
        );
    }

    @SuppressWarnings("unchecked")
    private static CacheStatistics getResponseValue(Response rs) {
        if (rs instanceof ValueResponse) {
            ValueResponse<CacheStatistics> actualRs = (ValueResponse<CacheStatistics>) rs;
            return actualRs.getValue();
        }
        throw new RuntimeException(((ExceptionResponse<?>)rs).getException());
    }

    private static void sendError(SocketIOClient client, String causeError) {
        log.debug("Will send error into UI - {}", causeError);
        client.sendEvent(UI_INFO_EXCHANGE_EVENT_TYPE, new GetCacheStatsUiErrorRs(causeError));
        client.disconnect();
    }

    private static Void sendError(SocketIOClient client, Throwable e) {
        log.debug("Will send error into UI - {}", e);
        client.sendEvent(UI_INFO_EXCHANGE_EVENT_TYPE, new GetCacheStatsUiErrorRs(e.getMessage()));
        client.disconnect();
        return null;
    }

    private static void sendSuccess(SocketIOClient client, CacheStatistics stats) {
        log.debug("Will send success stats into UI - {}", stats);
        client.sendEvent(UI_INFO_EXCHANGE_EVENT_TYPE, GetCacheStatsUiSuccessRs.of(stats));
    }
}