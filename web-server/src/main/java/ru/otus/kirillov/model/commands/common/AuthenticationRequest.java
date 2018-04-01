package ru.otus.kirillov.model.commands.common;


import ru.otus.kirillov.model.commands.emptyCommand.EmptyCommand.EmptyModelRequest;

public class AuthenticationRequest extends SessionModelRequestWrapper<EmptyModelRequest> {

    private AuthenticationRequest(String sessionId, String userName) {
        super(sessionId, userName, EmptyModelRequest.INSTANCE);
    }

    public static AuthenticationRequest of(String sessionId, String userName) {
        return new AuthenticationRequest(sessionId, userName);
    }
}
