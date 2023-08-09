package com.markvarga21.usermanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiError {
    private Date timeStamp;
    private HttpStatus status;
    private String message;
    private OperationType operationType;
    private String stackTrace;
}
