package com.kristof.szteam.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Helytelen jelszó"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "Az új jelszó nem egyezik meg"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "Fiók nincs aktiválva"),
    ACCOUNT_DISABLED(302, FORBIDDEN, "Fiók le van tiltva"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Jelszó vagy email helytelen")

    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
