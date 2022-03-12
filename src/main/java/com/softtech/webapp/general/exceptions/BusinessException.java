package com.softtech.webapp.general.exceptions;

import com.softtech.webapp.general.enums.IErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessException extends RuntimeException{

    private final IErrorMessage errorMessage;
    private final String message;

    public BusinessException(IErrorMessage baseErrorMessage) {
        this.errorMessage = baseErrorMessage;
        this.message = baseErrorMessage.getMessage();
    }

    public BusinessException(IErrorMessage baseErrorMessage, String entityName) {

        super(String.format(baseErrorMessage.getMessage(), entityName));

        this.errorMessage = baseErrorMessage;
        this.message = String.format(baseErrorMessage.getMessage(), entityName);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
