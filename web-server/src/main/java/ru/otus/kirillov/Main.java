package ru.otus.kirillov;

public class Main {

    private final static int PORT = 8090;

    public static void main(String[] args) throws Exception {

        WebServer server = new WebServer(PORT);
        server.start();
        server.join();

    }
}
