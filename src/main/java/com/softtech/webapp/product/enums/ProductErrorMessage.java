package com.softtech.webapp.product.enums;

import com.softtech.webapp.general.enums.IErrorMessage;

public enum ProductErrorMessage implements IErrorMessage {
    NAME_ALREADY_TAKEN("This product name is already exists!"),
    PRICE_BELOW_ONE("Price can't be lower than 1")
    ;

    private final String message;
    ProductErrorMessage(String message) {
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
