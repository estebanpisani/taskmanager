package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.hateoas.TaskModelAssembler;
import com.esteban.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private final TaskModelAssembler assembler;

    public TaskController(TaskService taskService, TaskModelAssembler assembler) {
        this.taskService = taskService;
        this.assembler = assembler;
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<TaskResponse>>> getAllTasks(){
        List<TaskResponse> tasks = this.taskService.getAllTasks();
        if(tasks.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(assembler.toCollectionModel(tasks));
    }

    @GetMapping("/{id}")
    public EntityModel<TaskResponse> getTaskById(@PathVariable String id){
        TaskResponse task = this.taskService.getTask(id);
        return assembler.toModel(task);
    }

    @PostMapping()
    public ResponseEntity<EntityModel<TaskResponse>> createTask(@RequestBody @Valid TaskRequest dto){
        TaskResponse newTask = taskService.createTask(dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+newTask.id()))
                .body(assembler.toModel(newTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TaskResponse>> updateTask(@PathVariable String id, @RequestBody TaskRequest dto){
        TaskResponse updatedTask = this.taskService.updateTask(id, dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+updatedTask.id()))
                .body(assembler.toModel(updatedTask));
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
