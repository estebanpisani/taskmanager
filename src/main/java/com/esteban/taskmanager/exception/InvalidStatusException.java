package com.esteban.taskmanager.exception;

import com.esteban.taskmanager.domain.enums.StatusEnum;

public final class InvalidStatusException extends TaskException {
    public InvalidStatusException(StatusEnum status) {
        super("La tarea ya está " + handleMessageByStatus(status));
    }

    private static String handleMessageByStatus(StatusEnum status) {
        return switch (status) {
            case TO_DO -> "pendiente.";
            case IN_PROGRESS -> "en progreso.";
            case DONE -> "terminada.";
        };
    }
}
