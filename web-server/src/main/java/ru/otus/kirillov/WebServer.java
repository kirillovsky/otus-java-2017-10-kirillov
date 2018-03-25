package ru.otus.kirillov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class WebServer {

    private static final Logger log = LogManager.getLogger();
    private static final String PORT_PROPERTY_NAME = "frontend.port";
    private static final String WEBAPP_RESOURCES_LOCATION = "webapp";
    private static final String PROPERTY_FILE_PATH = WEBAPP_RESOURCES_LOCATION + "/service.properties";
    private final Server server;

    public WebServer() {
        server = new Server(getServicePort());

        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(WEBAPP_RESOURCES_LOCATION + "/WEB-INF/web.xml");
        root.setResourceBase(geResourceBase(WEBAPP_RESOURCES_LOCATION));
        root.setParentLoaderPriority(true);
        server.setHandler(root);
    }

    private static int getServicePort() {
        int portNumber;
        try (InputStream inputStream = createResourceInputStream(PROPERTY_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            portNumber = Integer.valueOf(prop.getProperty(PORT_PROPERTY_NAME));
        } catch (Exception e) {
            log.catching(e);
            throw new IllegalStateException(e);
        }

        if (portNumber <= 0) {
            throw new IllegalStateException("Wrong port number - " + portNumber);
        }
        return portNumber;
    }

    private static String geResourceBase(String resourceName) {
        URL resourceURL = getURL(resourceName);
        try {
            return resourceURL.toURI().toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private static URL getURL(String resourceName) {
        URL resourceURL = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (resourceURL == null) {
            throw new IllegalArgumentException(String.format("Resource - %s not found", resourceName));
        }
        return resourceURL;
    }

    private static InputStream createResourceInputStream(String resourceName) {
        try {
            return getURL(resourceName).openStream();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
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
