package ru.otus.kirillov.model.services.remoteService.cache;

import ru.otus.kirillov.cacheengine.CacheEngine;
import ru.otus.kirillov.model.services.remoteService.AbstractService;
import ru.otus.kirillov.model.transport.MessageClient;
import ru.otus.kirillov.model.transport.Requests.Request;
import ru.otus.kirillov.model.transport.Responses.Response;

public class RemoteCacheEngineService extends AbstractService<CacheEngine>{

    public RemoteCacheEngineService(CacheEngine service, MessageClient<Response, Request> client) {
        super(service, client);
    }
}
