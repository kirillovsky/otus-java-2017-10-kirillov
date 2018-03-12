package ru.otus.kirillov;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.kirillov.model.service.dbOperationStub.DbWorkScheduledSimulator;
import ru.otus.kirillov.model.service.dbOperationStub.DbWorkSimulator;

import static ru.otus.kirillov.configuraton.WebServerConfiguration.*;

public class Main {

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "statics";
    private static DbWorkScheduledSimulator simulator;

    public static void main(String[] args) throws Exception {
        initScheduledSimulator();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(createMainServlet()), "/main");
        context.addServlet(new ServletHolder(createLoginServlet()), "/login");
        context.addServlet(new ServletHolder(createLogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(createGetCacheStatsServlet()), "/cache-stats");
        context.addServlet(new ServletHolder(createErrorPageServlet()), "/error");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();

        detachScheduledSimulator();
    }

    private static void initScheduledSimulator() {
        simulator = createDbWorkSimulatorScheduler();
        simulator.start();
    }

    private static void detachScheduledSimulator() {
        simulator.detach();
    }
}
