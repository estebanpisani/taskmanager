package com.esteban.taskmanager.exception;

public sealed class TaskException extends RuntimeException permits ResourceNotFoundException, InvalidStatusException {
    public TaskException(String message) {
        super(message);
    }
}
