package com.esteban.taskmanager.service;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;

import java.util.List;

public interface TaskService {

    List<TaskResponse> getAllTasks();

    TaskResponse getTask(String id);

    TaskResponse createTask(TaskRequest taskDto);

    TaskResponse updateTask(String id, TaskRequest taskDto);

    void startTask(String id);

    void doneTask(String id);

    void resetTask(String id);

    void deleteTask(String id);
}
