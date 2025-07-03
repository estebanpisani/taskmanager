package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public List<TaskResponse> getAllTasks(){
        return this.taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable String id){
        return this.taskService.getTask(id);
    }

    @PostMapping()
    public TaskResponse createTask(@RequestBody @Valid TaskRequest dto){
        return this.taskService.createTask(dto);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable String id, @RequestBody TaskRequest dto){
        return this.taskService.updateTask(id, dto);
    }

    @PatchMapping("/{id}/start")
    public void startTask(@PathVariable String id){
        this.taskService.startTask(id);
    }

    @PatchMapping("/{id}/done")
    public void doneTask(@PathVariable String id){
        this.taskService.doneTask(id);
    }

    @PatchMapping("/{id}/reset")
    public void resetTask(@PathVariable String id){
        this.taskService.resetTask(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id){
        this.taskService.deleteTask(id);
    }

}
