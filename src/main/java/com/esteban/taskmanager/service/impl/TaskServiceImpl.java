package com.esteban.taskmanager.service.impl;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.mapper.TaskMapper;
import com.esteban.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskServiceImpl(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return this.mapper.toResponseDtoList(this.repository.findAll());
    }

    @Override
    public TaskResponse getTask(String id) {
        return this.mapper.toResponseDto(this.repository.getReferenceById(UUID.fromString(id)));
    }

    @Override
    public TaskResponse createTask(TaskRequest taskDto) {
        return this.mapper.toResponseDto(this.repository.save(this.mapper.toEntity(taskDto)));
    }

    @Override
    public TaskResponse updateTask(String id, TaskRequest taskDto) {
        Optional<Task> taskOptional = Optional.of(this.repository.getReferenceById(UUID.fromString(id)));
        if(taskOptional.isPresent()) {
            Task updatedTask = this.mapper.updateEntityFromDto(taskDto, taskOptional.get());
            return this.mapper.toResponseDto(this.repository.save(updatedTask));
        }
        return null;
    }

    @Override
    public void startTask(String id) {
        Task task = this.repository.getReferenceById(UUID.fromString(id));
        task.setStatus(StatusEnum.IN_PROGRESS);
        this.repository.save(task);
    }

    @Override
    public void doneTask(String id) {
        Task task = this.repository.getReferenceById(UUID.fromString(id));
        task.setStatus(StatusEnum.DONE);
        this.repository.save(task);
    }

    @Override
    public void resetTask(String id) {
        Task task = this.repository.getReferenceById(UUID.fromString(id));
        task.setStatus(StatusEnum.TO_DO);
        this.repository.save(task);
    }

    @Override
    public void deleteTask(String id) {
        this.repository.deleteById(UUID.fromString(id));
    }
}
