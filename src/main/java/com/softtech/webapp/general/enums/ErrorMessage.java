package com.softtech.webapp.general.enums;

public enum ErrorMessage implements IErrorMessage {
    ITEM_NOT_FOUND("Item not found! -> %s"),
    ;

    private final String message;
    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
