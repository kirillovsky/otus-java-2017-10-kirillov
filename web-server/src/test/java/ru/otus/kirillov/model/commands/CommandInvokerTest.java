package ru.otus.kirillov.model.commands;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.commands.common.NotAuthModelResult;
import ru.otus.kirillov.model.services.auth.AuthService;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.otus.kirillov.model.commands.common.SessionModelRequestWrapper.of;

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
        ModelRequest modelRequestMock = mock(ModelRequest.class);

        when(commandMock.execute(modelRequestMock))
                .thenReturn(mock(ModelResult.class));

        ModelResult modelResult = invoker.execute(modelRequestMock);
        assertFalse(modelResult instanceof ErroneousModelResult);

        verify(commandMock, times(1)).isApplicable(modelRequestMock);
        verify(commandMock, times(1)).execute(modelRequestMock);
        verify(authServiceMock, never()).isValidSession(anyString(), anyString());
        verify(authServiceMock, never()).login(anyString(), anyString());
        verify(authServiceMock, never()).logout(anyString(), anyString());
    }

    private static final String DUMMY_USERNAME = "###DUMMY_USERNAME###";
    private static final String DUMMY_SESSION_ID = "###DUMMY_SESSION_ID###";

    @Test
    public void testSessionAuthSuccess() {
        ModelRequest modelRequestMock = mock(ModelRequest.class);
        ModelRequest sessionModelRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, modelRequestMock);

        when(commandMock.execute(modelRequestMock))
                .thenReturn(mock(ModelResult.class));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(true);

        ModelResult modelResult = invoker.execute(sessionModelRequest);
        assertFalse(modelResult instanceof NotAuthModelResult);

        verify(commandMock, times(1)).isApplicable(modelRequestMock);
        verify(commandMock, times(1)).execute(modelRequestMock);
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    @Test
    public void testSessionAuthError() {
        ModelRequest modelRequestMock = mock(ModelRequest.class);
        ModelRequest sessionModelRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, modelRequestMock);

        when(commandMock.execute(modelRequestMock))
                .thenReturn(mock(ModelResult.class));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(false);

        ModelResult modelResult = invoker.execute(sessionModelRequest);
        assertTrue(modelResult instanceof NotAuthModelResult);

        verify(commandMock, never()).execute(any());
        verify(commandMock, never()).isApplicable(any());
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    private final String DUMMY_ERRONEOUS_SESSION_ID = "!!!DUMMY_ERRONEOUS_SESSION_ID!!!";

    @Test
    public void testCommandErroneousResult() {
        ModelRequest modelRequestMock = mock(ModelRequest.class);
        ModelRequest sessionModelRequest = of(DUMMY_SESSION_ID, DUMMY_USERNAME, modelRequestMock);

        when(commandMock.execute(modelRequestMock))
                .thenReturn(ErroneousModelResult.of(DUMMY_ERRONEOUS_SESSION_ID));
        when(authServiceMock.isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME))
                .thenReturn(true);

        ErroneousModelResult result = (ErroneousModelResult) invoker.execute(sessionModelRequest);
        assertEquals(DUMMY_ERRONEOUS_SESSION_ID, result.getCause());

        verify(commandMock, times(1)).isApplicable(modelRequestMock);
        verify(commandMock, times(1)).execute(modelRequestMock);
        verify(authServiceMock, only()).isValidSession(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }
}