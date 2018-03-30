package ru.otus.kirillov.transport;

import ru.otus.kirillov.transport.Responses.Response;

public interface Observer<RS> {

    void notify(RS response);
}
