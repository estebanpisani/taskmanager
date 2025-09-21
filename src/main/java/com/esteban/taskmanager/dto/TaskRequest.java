package com.esteban.taskmanager.dto;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(
        @Schema(name = "Title", example = "Shopping list", required = true)
        @NotNull @NotBlank(message = "La tarea debe tener un título.") String title,
        @Schema(name = "Description", example = "A list with all items to buy", required = false)
        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres.") String description,
        @Schema(name = "Due date (dd-MM-yyyy'T'HH:mm)", example = "03-05-2070'T'15:30", required = false)
        @FutureOrPresent(message = "La fecha ingresada ya pasó.")
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm", timezone = "America/Argentina/Buenos_Aires") LocalDateTime dueDate,
        @Schema(name = "Priority", example = "MEDIUM", required = false)
        PriorityEnum priority
) {}
