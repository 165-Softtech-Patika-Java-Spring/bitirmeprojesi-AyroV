package com.softtech.webapp.general.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String errorDate;
    private String message;
    private String detail;
}
