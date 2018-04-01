package ru.otus.kirillov.model.commands.logout;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.model.commands.ModelResult;
import ru.otus.kirillov.model.commands.common.ErroneousModelResult;
import ru.otus.kirillov.model.services.auth.AuthService;

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
    private final static String DUMMY_SESSION_ID = "!!!DUMMY_PASSWORD!!!!";
    private final static String DUMMY_ERRONEOUS_MSG = "!!!DUMMY_ERRONEOUS_MSG####";

    @Test
    public void testSuccess() {
        ModelResult modelResult = command.execute(
                new LogOutModelRequest(DUMMY_SESSION_ID, DUMMY_USERNAME)
        );
        assertTrue(modelResult instanceof LogOutModelResult);
        verify(mockService, only()).logout(DUMMY_SESSION_ID, DUMMY_USERNAME);
    }

    @Test
    public void testError() {
        doThrow(new UnsupportedOperationException(DUMMY_ERRONEOUS_MSG))
                .when(mockService).logout(DUMMY_SESSION_ID, DUMMY_USERNAME);
        ErroneousModelResult erroneousResult = (ErroneousModelResult) command
                .execute(new LogOutModelRequest(DUMMY_SESSION_ID, DUMMY_USERNAME));
        assertEquals(DUMMY_ERRONEOUS_MSG, erroneousResult.getCause());
    }
}