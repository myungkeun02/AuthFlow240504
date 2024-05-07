package org.myungkeun.auth_flow.exception;

import lombok.Getter;
import org.myungkeun.auth_flow.exception.AuthFlowException;
import org.springframework.http.HttpStatus;

@Getter

public class NotFoundException extends AuthFlowException {
    public NotFoundException( String message) {
        super(HttpStatus.NOT_FOUND, message, HttpStatus.NOT_FOUND.value());
    }
}
