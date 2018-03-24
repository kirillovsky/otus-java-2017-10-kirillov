package ru.otus.kirillov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createErrorPageServlet;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createGetCacheStatsServlet;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createLoginServlet;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createLogoutServlet;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createMainServlet;

public class WebServer {

    private final static Logger log = LogManager.getLogger();
    private final static String PUBLIC_HTML = "./src/main/resources/statics";
    private final Server server;

    public WebServer(int portNumber) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(createMainServlet()), "/main");
        context.addServlet(new ServletHolder(createLoginServlet()), "/login");
        context.addServlet(new ServletHolder(createLogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(createGetCacheStatsServlet()), "/cache-stats");
        context.addServlet(new ServletHolder(createErrorPageServlet()), "/error");

        server = new Server(portNumber);
        server.setHandler(new HandlerList(resourceHandler, context));
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
