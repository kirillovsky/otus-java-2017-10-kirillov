package ru.otus.kirillov.model.services.remoteService.db;

import ru.otus.kirillov.model.services.remoteService.AbstractService;
import ru.otus.kirillov.model.transport.MessageClient;
import ru.otus.kirillov.model.transport.Requests.Request;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.service.DBService;

public class RemoteDBService extends AbstractService<DBService> {

    public RemoteDBService(DBService service, MessageClient<Response, Request> client) {
        super(service, client);
    }
}
