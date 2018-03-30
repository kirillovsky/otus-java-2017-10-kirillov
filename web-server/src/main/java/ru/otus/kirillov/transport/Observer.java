package ru.otus.kirillov.transport.notifiers;

import ru.otus.kirillov.transport.Responses.Response;

public interface Observer {

    void reseive(Response response);
}
