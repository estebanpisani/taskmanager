package com.esteban.taskmanager.service.impl;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.service.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    @Override
    public List<TaskResponse> getAllTasks() {
        return List.of();
    }

    @Override
    public TaskResponse getTask(String id) {
        return null;
    }

    @Override
    public TaskResponse createTask(TaskRequest taskDto) {
        return null;
    }

    @Override
    public TaskResponse updateTask(String id, TaskRequest taskDto) {
        return null;
    }

    @Override
    public void startTask(String id) {

    }

    @Override
    public void doneTask(String id) {

    }

    @Override
    public void deleteTask(String id) {

    }
}
