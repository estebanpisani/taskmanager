package com.esteban.taskmanager.exception;

public class GenericServerErrorException extends RuntimeException {
    public GenericServerErrorException() {
        super("Servicio no disponible. Intente nuevamente más tarde.");
    }
}
