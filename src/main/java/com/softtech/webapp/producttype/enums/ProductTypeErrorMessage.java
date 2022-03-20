package com.softtech.webapp.producttype.enums;

import com.softtech.webapp.general.enums.IErrorMessage;

public enum ProductTypeErrorMessage implements IErrorMessage {
    NAME_ALREADY_TAKEN("This product type name is already exists!"),
    ;

    private final String message;
    ProductTypeErrorMessage(String message) {
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
