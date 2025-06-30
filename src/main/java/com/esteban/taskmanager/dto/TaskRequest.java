package com.esteban.taskmanager.dto;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotNull @NotBlank(message = "La tarea debe tener un título") String title,
        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres") String description,
        @FutureOrPresent(message = "La fecha ingresada ya pasó.")
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm", timezone = "America/Argentina/Buenos_Aires") LocalDateTime dueDate,
        PriorityEnum priority
) {}
