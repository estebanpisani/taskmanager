package com.esteban.taskmanager.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String statusMessage) {
        super("La tarea ya está "+statusMessage);
    }
}
