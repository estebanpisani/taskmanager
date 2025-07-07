package com.esteban.taskmanager.exception;

import com.esteban.taskmanager.dto.ErrorResponse;
import com.esteban.taskmanager.dto.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.esteban.taskmanager.domain.enums.ErrorCodeEnum.INVALID_PARAMETERS;
import static com.esteban.taskmanager.domain.enums.ErrorCodeEnum.INVALID_STATUS;
import static com.esteban.taskmanager.domain.enums.ErrorCodeEnum.SERVER_ERROR;
import static com.esteban.taskmanager.domain.enums.ErrorCodeEnum.TASK_NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                TASK_NOT_FOUND
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){

        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Parámetros inválidos",
                fieldErrors,
                INVALID_PARAMETERS
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerError(GenericServerErrorException ex){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null,
                SERVER_ERROR
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatusError(InvalidStatusException ex){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null,
                INVALID_STATUS
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
