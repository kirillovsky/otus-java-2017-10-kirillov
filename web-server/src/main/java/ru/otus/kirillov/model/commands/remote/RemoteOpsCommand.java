package ru.otus.kirillov.model.commands.remote;

import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.transport.AsyncMessageClient;
import ru.otus.kirillov.model.transport.Responses.Response;
import ru.otus.kirillov.utils.CommonUtils;
import java.util.concurrent.CompletableFuture;

public class RemoteOpsCommand implements Command {

    private final AsyncMessageClient router;

    public RemoteOpsCommand(AsyncMessageClient router) {
        this.router = CommonUtils.retunIfNotNull(router);
    }

    @Override
    public boolean isApplicable(ModelRequest rq) {
        return rq instanceof RemoteOpsModelRequest;
    }

    @Override
    public ModelResult execute(ModelRequest rq) {
        RemoteOpsModelRequest remoteRq = (RemoteOpsModelRequest) CommonUtils.retunIfNotNull(rq);
        CompletableFuture<Response> result = router.send(remoteRq.getRequest());
        return new RemoteOpsModelResult<>(result);
    }
}
