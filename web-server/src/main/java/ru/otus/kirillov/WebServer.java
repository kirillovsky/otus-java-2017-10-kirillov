package ru.otus.kirillov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import java.net.URL;

public class WebServer {

    private static final Logger log = LogManager.getLogger();
    private static final String WEBAPP_RESOURCES_LOCATION = "webapp";
    private final Server server;

    public WebServer(int portNumber) throws Exception {
        server = new Server(portNumber);

        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(WEBAPP_RESOURCES_LOCATION + "/WEB-INF/web.xml");
        URL webAppDir = Thread.currentThread().getContextClassLoader().getResource(WEBAPP_RESOURCES_LOCATION);
        if (webAppDir == null) {
            throw new RuntimeException(String.format("No %s directory was found into the JAR file", WEBAPP_RESOURCES_LOCATION));
        }
        root.setResourceBase(webAppDir.toURI().toString());
        root.setParentLoaderPriority(true);
        server.setHandler(root);
    }

    public void start() throws Exception {
        server.start();
        log.info("Server started!!!");
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void safelyStop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error("Faulty server stops!");
            log.catching(e);
        }
    }
}
