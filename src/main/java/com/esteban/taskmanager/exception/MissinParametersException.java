package com.esteban.taskmanager.exception;

public class MissinParametersException extends RuntimeException {
    public MissinParametersException(String message) {
        super("Faltan parámetros obligatorios: "+message);
    }
}
