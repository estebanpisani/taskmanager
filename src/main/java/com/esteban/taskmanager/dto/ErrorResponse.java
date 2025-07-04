package com.esteban.taskmanager.dto;

import com.esteban.taskmanager.domain.enums.ErrorCodeEnum;
import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        HttpStatus status,
        String message,
        List<FieldErrorResponse> fieldErrors,
        ErrorCodeEnum errorCode
){}