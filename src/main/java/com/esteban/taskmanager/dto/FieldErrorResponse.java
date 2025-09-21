package com.esteban.taskmanager.dto;

public record FieldErrorResponse(
        String field,
        String message
) {
}
