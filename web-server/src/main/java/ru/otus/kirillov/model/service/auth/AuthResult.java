package ru.otus.kirillov.model.service.auth;

import ru.otus.kirillov.utils.CommonUtils;

public class AuthResult{

    private final AuthStatus status;

    private final String additionalInfo;

    protected AuthResult(AuthStatus status, String additionalInfo) {
        this.status = CommonUtils.retunIfNotNull(status);
        this.additionalInfo = CommonUtils.retunIfNotNull(additionalInfo);
    }

    public AuthStatus getStatus() {
        return status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public static AuthResult of(AuthStatus status) {
        CommonUtils.requiredNotNull(status);
        return new AuthResult(status, status.name());
    }

    public static AuthResult of(AuthStatus status, String additionalInfo) {
        CommonUtils.requiredNotNull(status, additionalInfo);
        return new AuthResult(status, additionalInfo);
    }
}
