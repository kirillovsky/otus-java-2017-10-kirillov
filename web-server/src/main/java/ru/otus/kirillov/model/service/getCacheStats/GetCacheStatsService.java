package ru.otus.kirillov.model.service.getCacheStats;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.kirillov.cacheengine.stats.CacheStatistics;
import ru.otus.kirillov.model.commands.getCacheStats.GetCacheStatsResult;
import ru.otus.kirillov.model.service.getCacheStats.messages.GetCacheStatsUiRq;
import ru.otus.kirillov.utils.CommonUtils;

public class GetCacheStatsService {

    private static final Logger log = LogManager.getLogger();

    private final String hostName;
    private final String endpointName;
    private final int portName;

    private SocketIOServer server;

    public GetCacheStatsService(String hostName, String endpointName, String portName) {
        this.hostName = CommonUtils.retunIfNotNull(hostName);
        this.endpointName = CommonUtils.retunIfNotNull(endpointName);
        this.portName = Integer.valueOf(CommonUtils.retunIfNotNull(portName));
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
        endpoint.addEventListener("message", GetCacheStatsUiRq.class, (client, data, ackSender) -> {
            log.debug("Message received: " + data);
            client.sendEvent("message", GetCacheStatsResult.of(CacheStatistics.of(0, 0, 0)));
        });
        return server;
    }
}