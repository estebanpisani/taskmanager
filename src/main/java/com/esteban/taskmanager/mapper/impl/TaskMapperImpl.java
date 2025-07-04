package com.esteban.taskmanager.mapper.impl;

import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(TaskRequest taskDto) {
        return new Task(
                taskDto.title(),
                taskDto.description(),
                taskDto.dueDate(),
                taskDto.priority(),
                StatusEnum.TO_DO
        );
    }

    @Override
    public Task updateEntityFromDto(TaskRequest dto, Task entity) {

        if(nonNull(dto.title())){
            entity.setTitle(dto.title());
        }
        if(nonNull(dto.description())){
            entity.setDescription(dto.description());
        }
        if(nonNull(dto.dueDate())){
            entity.setDueDateTime(dto.dueDate());
        }
        if(nonNull(dto.priority())){
            entity.setPriority(dto.priority());
        }

        entity.setUpdateAt(LocalDateTime.now());

        return entity;
    }

    @Override
    public TaskResponse toResponseDto(Task entity) {
        return new TaskResponse(
                entity.getId().toString(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDateTime(),
                entity.getPriority(),
                entity.getStatus()
        );
    }

    @Override
    public List<TaskResponse> toResponseDtoList(List<Task> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

}
