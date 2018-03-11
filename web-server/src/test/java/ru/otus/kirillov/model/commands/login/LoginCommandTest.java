package ru.otus.kirillov.model.commands.login;

import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.model.commands.Result;
import ru.otus.kirillov.model.commands.common.ErroneousResult;
import ru.otus.kirillov.model.service.auth.AuthService;
import ru.otus.kirillov.model.service.auth.StubAuthServiceImpl;
import ru.otus.kirillov.utils.AESSecurity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static ru.otus.kirillov.model.service.auth.StubAuthServiceImpl.*;

public class LoginCommandTest {

    private LoginCommand command;
    private AuthService authServiceMock;
    private AESSecurity aesSecurityMock;

    @Before
    public void setUp() throws Exception {
        authServiceMock = mock(AuthService.class);
        aesSecurityMock = mock(AESSecurity.class);
        command = new LoginCommand(authServiceMock, aesSecurityMock);

        when(aesSecurityMock.encrypt(DUMMY_PASSWORD))
                .thenReturn(DUMMY_ENCRYPTED_PASSWORD);
    }

    private static final String DUMMY_USERNAME = "###DUMMY_USERNAME###";

    private static final String DUMMY_PASSWORD = "###DUMMY_PASSWORD###";
    private static final String DUMMY_ENCRYPTED_PASSWORD = "###DUMMY_ENCRYPTED_PASSWORD###";

    private static final String DUMMY_SESSION_ID = "###DUMMY_SESSION_ID###";

    @Test
    public void testSuccess() {
        when(authServiceMock.login(DUMMY_USERNAME, DUMMY_ENCRYPTED_PASSWORD))
                .thenReturn(DUMMY_SESSION_ID);

        Result result = command.execute(LoginRequest.of(DUMMY_USERNAME, DUMMY_PASSWORD));
        LoginResult loginResult = (LoginResult) result;

        assertEquals(DUMMY_USERNAME, loginResult.getUserName());
        assertEquals(DUMMY_SESSION_ID, loginResult.getSessionId());
        verifyMockCallCount();
    }

    @Test
    public void testWrongLoginOrPassword() {
        when(authServiceMock.login(DUMMY_USERNAME, DUMMY_ENCRYPTED_PASSWORD))
                .thenThrow(new NotAuthorizedException(DUMMY_USERNAME, DUMMY_PASSWORD));

        ErroneousResult result =
                (ErroneousResult) command.execute(LoginRequest.of(DUMMY_USERNAME, DUMMY_PASSWORD));

        assertNotNull(result.getCause());

        verifyMockCallCount();
    }

    private void verifyMockCallCount() {
        verify(aesSecurityMock, times(1))
                .encrypt(anyString());
        verify(authServiceMock, only())
                .login(DUMMY_USERNAME, DUMMY_ENCRYPTED_PASSWORD);
    }
}