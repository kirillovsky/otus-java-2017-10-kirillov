package ru.otus.kirillov;

public class Main {

    public static void main(String[] args) throws Exception {

        WebServer server = new WebServer();
        server.start();
        server.join();

    }
}
