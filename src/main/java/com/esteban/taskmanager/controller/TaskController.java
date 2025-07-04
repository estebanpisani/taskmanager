package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllTasks(){
        List<TaskResponse> tasks = this.taskService.getAllTasks();
        return tasks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id){
        TaskResponse task = this.taskService.getTask(id);
        return ResponseEntity
                .ok()
                .body(task);
    }

    @PostMapping()
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRequest dto){
        TaskResponse newTask = taskService.createTask(dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+newTask.id()))
                .body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody TaskRequest dto){
        TaskResponse updatedTask = this.taskService.updateTask(id, dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+updatedTask.id()))
                .body(updatedTask);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<?> startTask(@PathVariable String id){
        this.taskService.startTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<?> doneTask(@PathVariable String id){
        this.taskService.doneTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reset")
    public ResponseEntity<?> resetTask(@PathVariable String id){
        this.taskService.resetTask(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id){
        this.taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

}
