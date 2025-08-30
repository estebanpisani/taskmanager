package com.esteban.taskmanager.service;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.mapper.TaskMapper;
import com.esteban.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final String DESCRIPTION = "Descripcion de la tarea";
    private static final String TASK_TITLE = "Tarea";
    @Mock
    TaskRepository repository;
    @Mock
    TaskMapper mapper;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {

        Task task = createTaskEntity(TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskRequest request = createTaskRequest(TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH);
        TaskResponse expectedResponse = createTaskResponse(task.getId().toString(), TASK_TITLE + "1", DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);

        when(repository.save(ArgumentMatchers.any(Task.class))).thenReturn(task);
        when(mapper.toEntity(ArgumentMatchers.any(TaskRequest.class))).thenReturn(task);
        when(mapper.toResponseDto(ArgumentMatchers.any(Task.class))).thenReturn(expectedResponse);

        // when
        TaskResponse result = taskService.createTask(request);

        // then
        assertNotNull(result);
        assertResult(expectedResponse, result);
        verify(repository, times(1)).save(task);
    }

    private static void assertResult(TaskResponse expectedResult, TaskResponse actualResult) {
        assertEquals(expectedResult.id(), actualResult.id());
        assertEquals(expectedResult.title(), actualResult.title());
        assertEquals(expectedResult.description(), actualResult.description());
        assertEquals(expectedResult.dueDate(), actualResult.dueDate());
        assertEquals(expectedResult.priority(), actualResult.priority());
        assertEquals(expectedResult.status(), actualResult.status());
    }

    private static Task createTaskEntity(String title, String description, LocalDateTime dueDateTime, PriorityEnum priority, StatusEnum status) {
        return new Task(title, description, dueDateTime, priority, status);
    }

    private static TaskRequest createTaskRequest(String title, String description, LocalDateTime dueDateTime, PriorityEnum priority) {
        return new TaskRequest(title, description, dueDateTime, priority);
    }

    private static TaskResponse createTaskResponse(String id, String title, String description, LocalDateTime dueDateTime, PriorityEnum priority, StatusEnum status) {
        return new TaskResponse(id, title, description, dueDateTime, priority, status);
    }

}
