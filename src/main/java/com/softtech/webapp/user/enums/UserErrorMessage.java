package com.softtech.webapp.user.enums;

import com.softtech.webapp.general.enums.IErrorMessage;

public enum UserErrorMessage implements IErrorMessage {
    USERNAME_ALREADY_TAKEN("This username is already exists!"),
    ID_USERNAME_NOT_MATCH("Given id and username doesn't match"),
    ;

    private final String message;
    UserErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
