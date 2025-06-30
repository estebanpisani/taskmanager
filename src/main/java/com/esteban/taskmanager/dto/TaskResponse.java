package com.esteban.taskmanager.dto;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskResponse(
        String id,
        String title,
        String description,
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm", timezone = "America/Argentina/Buenos_Aires") LocalDateTime dueDate,
        PriorityEnum priority,
        StatusEnum status
) {
}
