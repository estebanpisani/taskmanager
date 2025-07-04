package com.esteban.taskmanager.exception;

public class InvalidParametersException extends RuntimeException {
    public InvalidParametersException(String param) {
        super("Parámetro "+param+" inválido.");
    }
}
