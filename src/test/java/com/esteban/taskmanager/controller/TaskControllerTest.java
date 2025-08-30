package com.esteban.taskmanager.controller;

import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.hateoas.TaskModelAssembler;
import com.esteban.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    private static final String DESCRIPTION = "Descripcion de la tarea";
    private static final String TASK_TITLE = "Tarea";
    private static final String TASKS_BASE_PATH = "/tasks";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskModelAssembler assembler;

    @Test
    void shouldReturnTaskOnGetById() throws Exception {
        TaskResponse response = createTaskResponse("1", TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        when(taskService.getTask("1")).thenReturn(response);
        when(assembler.toModel(response)).thenReturn(EntityModel.of(response));

        mockMvc.perform(get(TASKS_BASE_PATH + "/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TASK_TITLE + "1"));
    }

    private static TaskResponse createTaskResponse(String id, String title, String description, LocalDateTime dueDateTime, PriorityEnum priority, StatusEnum status) {
        return new TaskResponse(id, title, description, dueDateTime, priority, status);
    }
}
