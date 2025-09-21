package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.hateoas.TaskModelAssembler;
import com.esteban.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    private static final String DESCRIPTION = "Descripcion de la tarea";
    private static final String TASK_TITLE = "Tarea";
    private static final String TASKS_BASE_PATH = "/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskModelAssembler assembler;

    //Test GET By ID
    @Test
    void shouldReturnTaskOnGetById() throws Exception {
        TaskResponse response = createTaskResponse("1", TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        when(taskService.getTask("1")).thenReturn(response);
        when(assembler.toModel(response)).thenReturn(EntityModel.of(response));

        mockMvc.perform(get(TASKS_BASE_PATH + "/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TASK_TITLE + "1"));
    }

    @Test
    void shouldReturnAllTasksOnGetAllTasks() throws Exception {
        List<TaskResponse> taskList = createTaskList(3);
        when(taskService.getAllTasks()).thenReturn(taskList);
        when(assembler.toCollectionModel(taskList))
                .thenReturn(CollectionModel.of(
                        taskList.stream()
                                .map(EntityModel::of)
                                .toList()
                ));

        mockMvc.perform(get(TASKS_BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.taskResponseList.length()").value(3))
                .andExpect(jsonPath("$._embedded.taskResponseList[0].title").value(TASK_TITLE+0))
                .andExpect(jsonPath("$._embedded.taskResponseList[1].title").value(TASK_TITLE+1))
                .andExpect(jsonPath("$._embedded.taskResponseList[2].title").value(TASK_TITLE+2));
    }

    @Test
    void shouldReturnEmptyOnGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(emptyList());

        mockMvc.perform(get(TASKS_BASE_PATH))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNewTaskOnCreateTask() throws Exception {
        TaskResponse createdTask = createTaskResponse("1", TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskRequest taskRequest = createTaskRequest(TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH);
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(createdTask);
        when(assembler.toModel(createdTask)).thenReturn(EntityModel.of(createdTask));

        mockMvc.perform(post(TASKS_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(TASK_TITLE + "1"));
    }

    @Test
    void shouldReturnBadRequestOnCreateTaskWithInvalidParams() throws Exception {
        TaskRequest invalidRequest = createTaskRequest(null, DESCRIPTION, LocalDateTime.now().minusYears(1), PriorityEnum.LOW);

        mockMvc.perform(post(TASKS_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdatedTaskOnUpdateTask() throws Exception {
        TaskResponse updatedTask = createTaskResponse("1", TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskRequest taskRequest = createTaskRequest(TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH);
        when(taskService.updateTask(anyString(), any(TaskRequest.class))).thenReturn(updatedTask);
        when(assembler.toModel(updatedTask)).thenReturn(EntityModel.of(updatedTask));

        mockMvc.perform(put(TASKS_BASE_PATH + "/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(TASK_TITLE + "1"));
    }

    @Test
    void shouldReturnBadRequestOnUpdateTaskWithInvalidParams() throws Exception {
        TaskRequest invalidRequest = createTaskRequest(null, DESCRIPTION, LocalDateTime.now().minusYears(1), PriorityEnum.LOW);

        mockMvc.perform(put(TASKS_BASE_PATH + "/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkOnStartTask() throws Exception {
        mockMvc.perform(patch(TASKS_BASE_PATH + "/{id}/start", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkOnDoneTask() throws Exception {
        mockMvc.perform(patch(TASKS_BASE_PATH + "/{id}/done", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkOnResetTask() throws Exception {
        mockMvc.perform(patch(TASKS_BASE_PATH + "/{id}/reset", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkOnDeleteTask() throws Exception {
        mockMvc.perform(delete(TASKS_BASE_PATH + "/{id}", "1"))
                .andExpect(status().isOk());
    }

    private static TaskResponse createTaskResponse(String id, String title, String description, LocalDateTime dueDateTime, PriorityEnum priority, StatusEnum status) {
        return new TaskResponse(id, title, description, dueDateTime, priority, status);
    }

    private static TaskRequest createTaskRequest(String title, String description, LocalDateTime dueDateTime, PriorityEnum priority) {
        return new TaskRequest(title, description, dueDateTime, priority);
    }

    private static List<TaskResponse> createTaskList(int length) {
        List<TaskResponse> tasks = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            tasks.add(createTaskResponse(String.valueOf(i), TASK_TITLE+i, DESCRIPTION+i, LocalDateTime.now().plusYears(i), PriorityEnum.MEDIUM, StatusEnum.TO_DO));
        }
        return tasks;
    }
}
