package com.esteban.taskmanager.service;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.esteban.taskmanager.dto.TaskRequest;
import com.esteban.taskmanager.dto.TaskResponse;
import com.esteban.taskmanager.exception.InvalidStatusException;
import com.esteban.taskmanager.exception.ResourceNotFoundException;
import com.esteban.taskmanager.mapper.TaskMapper;
import com.esteban.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final String DESCRIPTION = "Descripcion de la tarea";
    private static final String TASK_TITLE = "Tarea";
    private static final String DEFAULT_UUID = UUID.randomUUID().toString();
    private static final String ALREADY_PENDING_TASK_MESSAGE = "La tarea ya está pendiente.";
    private static final String ALREADY_DONE_TASK_MESSAGE = "La tarea ya está terminada.";
    private static final String ALREADY_IN_PROGRESS_TASK_MESSAGE = "La tarea ya está en progreso.";

    @Mock
    TaskRepository repository;
    @Mock
    TaskMapper mapper;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void shouldRetrieveAllTasksSuccessfully() {

        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        List<Task> taskList = singletonList(task);
        List<TaskResponse> expectedResponse = singletonList(createTaskResponse(task.getId().toString(), TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO));

        when(repository.findAll()).thenReturn(taskList);
        when(mapper.toResponseDtoList(ArgumentMatchers.anyList())).thenReturn(expectedResponse);

        List<TaskResponse> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertResult(expectedResponse.get(0), result.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldRetrieveTaskSuccessfully() {

        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskResponse expectedResponse = createTaskResponse(task.getId().toString(), TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedResponse);

        TaskResponse result = taskService.getTask(task.getId().toString());

        assertNotNull(result);
        assertResult(expectedResponse, result);
        verify(repository, times(1)).findById(any(UUID.class));
    }


    @Test
    void shouldThrowResourceNotFoundWhenGetTaskNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(DEFAULT_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("No se encontró la tarea con id: %s", DEFAULT_UUID));

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldCreateTaskSuccessfully() {

        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskRequest request = createTaskRequest(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH);
        TaskResponse expectedResponse = createTaskResponse(task.getId().toString(), TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);

        when(repository.save(any(Task.class))).thenReturn(task);
        when(mapper.toEntity(any(TaskRequest.class))).thenReturn(task);
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedResponse);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertResult(expectedResponse, result);
        verify(repository, times(1)).save(task);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        TaskRequest request = createTaskRequest(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH);
        TaskResponse expectedResponse = createTaskResponse(task.getId().toString(), TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);

        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);
        when(mapper.updateEntityFromDto(any(TaskRequest.class), any(Task.class))).thenReturn(task);
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedResponse);

        TaskResponse result = taskService.updateTask(task.getId().toString(), request);

        assertNotNull(result);
        assertResult(expectedResponse, result);
        verify(repository, times(1)).save(task);
        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    void shouldThrowResourceNotFoundWhenUpdatingTaskNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(DEFAULT_UUID, any(TaskRequest.class)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("No se encontró la tarea con id: %s", DEFAULT_UUID));

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldStartTaskSuccessfully() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        taskService.startTask(DEFAULT_UUID);

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(1)).save(task);
    }

    @Test
    void shouldNotStartInProgressTask() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.IN_PROGRESS);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.startTask(DEFAULT_UUID))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessageContaining(ALREADY_IN_PROGRESS_TASK_MESSAGE);

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(task);
    }

    @Test
    void shouldThrowResourceNotFoundWhenStartingTaskNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.startTask(DEFAULT_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("No se encontró la tarea con id: %s", DEFAULT_UUID));

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldDoneTaskSuccessfully() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        taskService.doneTask(task.getId().toString());

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(1)).save(task);
    }

    @Test
    void shouldNotStopDoneTask() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.DONE);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.doneTask(DEFAULT_UUID))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessageContaining(ALREADY_DONE_TASK_MESSAGE);

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(task);
    }

    @Test
    void shouldThrowResourceNotFoundWhenEndingTaskNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.doneTask(DEFAULT_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("No se encontró la tarea con id: %s", DEFAULT_UUID));

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldResetTaskSuccessfully() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.IN_PROGRESS);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        taskService.resetTask(task.getId().toString());

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(1)).save(task);
    }

    @Test
    void shouldNotResetPendingTask() {
        Task task = createTaskEntity(TASK_TITLE + DEFAULT_UUID, DESCRIPTION, LocalDateTime.now().plusYears(1), PriorityEnum.HIGH, StatusEnum.TO_DO);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.resetTask(DEFAULT_UUID))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessageContaining(ALREADY_PENDING_TASK_MESSAGE);

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(task);
    }

    @Test
    void shouldThrowResourceNotFoundWhenToResetTaskNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.resetTask(DEFAULT_UUID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("No se encontró la tarea con id: %s", DEFAULT_UUID));

        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        taskService.deleteTask(DEFAULT_UUID);

        verify(repository, times(1)).deleteById(any(UUID.class));
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
