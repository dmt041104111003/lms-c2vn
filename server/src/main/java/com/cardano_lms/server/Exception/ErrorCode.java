package com.cardano_lms.server.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1009, "Role not existed", HttpStatus.NOT_FOUND),
    NONCE_NOT_EXISTED(1010, "Nonce not existed", HttpStatus.BAD_REQUEST),
    LOGIN_METHOD_IS_REQUIRED(1011, "Login method is required", HttpStatus.BAD_REQUEST),
    METHOD_HAS_BEEN_EXISTED(1012, "This method has been existed", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1013, "Password required", HttpStatus.BAD_REQUEST),
    MISSING_CREDENTIALS(1014, "Missing credential", HttpStatus.BAD_REQUEST),
    LOGIN_METHOD_NOT_SUPPORTED(1015, "Login method not supported", HttpStatus.BAD_REQUEST),
    DONT_HAVE_PERMISSION_WITH_WALLET(1016, "You dont have permission with this wallet", HttpStatus.BAD_REQUEST),
    WALLET_ADDRESS_REQUIRED(1017, "Wallet address is required", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1018, "Email is required", HttpStatus.BAD_REQUEST),
    SOCIAL_LINK_NOT_FOUND(1019, "Social link not found", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1020, "Not found", HttpStatus.NOT_FOUND),
    INVALID_ARGUMENT(1021, "Invalid argument", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_USED(1022, "Email has been used by another people", HttpStatus.BAD_REQUEST),
    WALLET_ALREADY_USED(1022, "Wallet has been used by another people", HttpStatus.BAD_REQUEST),
            ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
