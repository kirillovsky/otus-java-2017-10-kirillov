package ru.otus.kirillov.transport.notifiers;

import ru.otus.kirillov.transport.Operations.Operation;

public interface Observable {

    void send(Observable observable, Operation remoteOp);
}
