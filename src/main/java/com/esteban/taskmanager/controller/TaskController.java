package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.hateoas.TaskModelAssembler;
import com.esteban.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get all tasks", description = "Returns a list of all task found in data base")
    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<TaskResponse>>> getAllTasks(){
        List<TaskResponse> tasks = this.taskService.getAllTasks();
        return tasks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(assembler.toCollectionModel(tasks));
    }

    @Operation(summary = "Get task by Id", description = "Returns a specific task searching by id parameter.")
    @GetMapping("/{id}")
    public EntityModel<TaskResponse> getTaskById(@PathVariable String id){
        TaskResponse task = this.taskService.getTask(id);
        return assembler.toModel(task);
    }

    @Operation(summary = "Create a task", description = "Save the task in data base and returns it.")
    @PostMapping()
    public ResponseEntity<EntityModel<TaskResponse>> createTask(@RequestBody @Valid TaskRequest dto){
        TaskResponse newTask = taskService.createTask(dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+newTask.id()))
                .body(assembler.toModel(newTask));
    }

    @Operation(summary = "Update a task", description = "Modify params for a specific task with the id provided.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TaskResponse>> updateTask(@PathVariable String id, @RequestBody @Valid TaskRequest dto){
        TaskResponse updatedTask = this.taskService.updateTask(id, dto);
        return ResponseEntity
                .created(URI.create("/tasks/"+updatedTask.id()))
                .body(assembler.toModel(updatedTask));
    }

    @Operation(summary = "Start a task", description = "Change a not started task to the state of IN_PROGRESS.")
    @PatchMapping("/{id}/start")
    public void startTask(@PathVariable String id){
        this.taskService.startTask(id);
    }

    @Operation(summary = "Done a task", description = "Change an in progress task to the state of DONE.")
    @PatchMapping("/{id}/done")
    public void doneTask(@PathVariable String id){
        this.taskService.doneTask(id);
    }

    @Operation(summary = "Reset a task", description = "Change an in progress or done task to the state of TO_DO.")
    @PatchMapping("/{id}/reset")
    public void resetTask(@PathVariable String id){
        this.taskService.resetTask(id);
    }

    @Operation(summary = "Delete a task", description = "Delete a task with the id provided.")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id){
        this.taskService.deleteTask(id);
    }

}
