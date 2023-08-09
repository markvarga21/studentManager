package com.markvarga21.usermanager.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private OperationType operationType;
    public UserNotFoundException(String message, OperationType operationType) {
        super(message);
        this.operationType = operationType;
    }
}

