package com.softtech.webapp.general.exceptions;

import com.softtech.webapp.general.enums.IErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends BusinessException{

    public ItemNotFoundException(IErrorMessage message) {
        super(message);
    }

    public ItemNotFoundException(IErrorMessage baseErrorMessage, String message) {
        super(baseErrorMessage, message);
    }
}
