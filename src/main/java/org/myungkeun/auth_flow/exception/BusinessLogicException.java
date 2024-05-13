package org.myungkeun.auth_flow.exception;

import org.springframework.http.HttpStatus;


public class BusinessLogicException extends AuthFlowException {
    public BusinessLogicException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
