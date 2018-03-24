package ru.otus.kirillov;

import ru.otus.kirillov.model.service.dbOperationStub.DbWorkScheduledSimulator;
import static ru.otus.kirillov.configuraton.WebServerConfiguration.createDbWorkSimulatorScheduler;

public class Main {

    private final static int PORT = 8090;
    private static DbWorkScheduledSimulator simulator;

    public static void main(String[] args) throws Exception {
        initScheduledSimulator();

        WebServer server = new WebServer(PORT);
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
