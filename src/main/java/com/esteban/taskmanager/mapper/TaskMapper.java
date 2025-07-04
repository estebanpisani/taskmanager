package com.esteban.taskmanager.mapper;

import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface TaskMapper {

    Task toEntity(TaskRequest taskDto);

    Task updateEntityFromDto(TaskRequest dto, Task entity);

    TaskResponse toResponseDto(Task entity);

    List<TaskResponse> toResponseDtoList(List<Task> entities);
}
