package ru.otus.kirillov.model.commands;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.commands.common.NotAuthResult;
import ru.otus.kirillov.model.service.auth.AuthService;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.otus.kirillov.model.commands.common.SessionRequestWrapper.of;

public class CommandInvokerTest {

    private CommandInvoker invoker;

    private AuthService authServiceMock;

    private Command commandMock;

    @Before
    public void setUp() {
        authServiceMock = mock(AuthService.class);
        commandMock = mock(Command.class);
        invoker = new CommandInvoker(authServiceMock,
                Collections.singletonList(commandMock));

        when(commandMock.isApplicable(any()))
                .thenReturn(true);
    }

    @Test
    public void testNonSessionRequest() {
        Request requestMock = mock(Request.class);

        when(commandMock.execute(requestMock))
                .thenReturn(mock(Result.class));

        Result result = invoker.execute(requestMock);
        assertFalse(result instanceof ErroneousResult);

        verify(commandMock, times(1)).isApplicable(requestMock);
        verify(commandMock, times(1)).execute(requestMock);
        verify(authServiceMock, never()).isValidSession(anyString(), anyString());
        verify(authServiceMock, never()).login(anyString(), anyString());
        verify(authServiceMock, never()).logout(anyString(), anyString());
    }

    private static final String DUMMY_USERNAME = "###DUMMY_USERNAME###";
    private static final String DUMMY_SESSION_ID = "###DUMMY_SESSION_ID###";

    @Test
    public void testSessionAuthSuccess() {
        Request requestMock = mock(Request.class);
        Request sessionRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, requestMock);

        when(commandMock.execute(requestMock))
                .thenReturn(mock(Result.class));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(true);

        Result result = invoker.execute(sessionRequest);
        assertFalse(result instanceof NotAuthResult);

        verify(commandMock, times(1)).isApplicable(requestMock);
        verify(commandMock, times(1)).execute(requestMock);
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    @Test
    public void testSessionAuthError() {
        Request requestMock = mock(Request.class);
        Request sessionRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, requestMock);

        when(commandMock.execute(requestMock))
                .thenReturn(mock(Result.class));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(false);

        Result result = invoker.execute(sessionRequest);
        assertTrue(result instanceof NotAuthResult);

        verify(commandMock, never()).execute(any());
        verify(commandMock, never()).isApplicable(any());
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    private final String DUMMY_ERRONEOUS_SESSION_ID = "!!!DUMMY_ERRONEOUS_SESSION_ID!!!";

    @Test
    public void testCommandErroneousResult() {
        Request requestMock = mock(Request.class);
        Request sessionRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, requestMock);

        when(commandMock.execute(requestMock))
                .thenReturn(ErroneousResult.of(DUMMY_ERRONEOUS_SESSION_ID));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(true);

        ErroneousResult result = (ErroneousResult) invoker.execute(sessionRequest);
        assertEquals(DUMMY_ERRONEOUS_SESSION_ID, result.getCause());

        verify(commandMock, times(1)).isApplicable(requestMock);
        verify(commandMock, times(1)).execute(requestMock);
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }
}