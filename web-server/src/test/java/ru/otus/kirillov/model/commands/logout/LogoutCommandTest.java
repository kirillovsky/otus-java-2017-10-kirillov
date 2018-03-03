package ru.otus.kirillov.model.commands.logout;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.service.auth.AuthService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LogoutCommandTest {

    private LogoutCommand command;
    private AuthService mockService;

    @Before
    public void setUp() {
        mockService = mock(AuthService.class);
        command = new LogoutCommand(mockService);
    }

    private final static String DUMMY_USERNAME = "!!!DUMMY_USERNAME!!!!";
    private final static String DUMMY_SESSION_ID = "!!!DUMMY_SESSION_ID!!!!";
    private final static String DUMMY_ERRONEOUS_MSG = "!!!DUMMY_ERRONEOUS_MSG####";

    @Test
    public void testSuccess() {
        Result result = command.execute(
                new LogOutRequest(DUMMY_SESSION_ID, DUMMY_USERNAME)
        );
        assertTrue(result instanceof LogOutResult);
        verify(mockService, only()).logout(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    @Test
    public void testError() {
        doThrow(new UnsupportedOperationException(DUMMY_ERRONEOUS_MSG))
                .when(mockService).logout(DUMMY_SESSION_ID, DUMMY_USERNAME);
        Result result = command.execute(
                new LogOutRequest(DUMMY_SESSION_ID, DUMMY_USERNAME)
        );
        assertTrue(result instanceof ErroneousResult);
        ErroneousResult erroneousResult = (ErroneousResult) result;
        assertEquals(DUMMY_ERRONEOUS_MSG, erroneousResult.getCause());
    }
}