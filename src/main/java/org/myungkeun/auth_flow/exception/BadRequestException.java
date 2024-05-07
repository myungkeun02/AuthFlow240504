package org.myungkeun.auth_flow.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class BadRequestException extends AuthFlowException{
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message, HttpStatus.BAD_REQUEST.value());
    }
}
