package com.esteban.taskmanager.repository;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSaveAndFindTaskById() {
        Task task = createTaskEntity();

        taskRepository.save(task);
        Task foundTask = taskRepository.findById(task.getId()).orElse(null);

        assertNotNull(foundTask);

        assertResult(task, foundTask);
    }

    @Test
    void shouldFindAndUpdateTaskDescription() {
        Task newTask = new Task("T1", "Desc", LocalDateTime.now().plusDays(5), PriorityEnum.HIGH, StatusEnum.TO_DO);

        taskRepository.save(newTask);

        Task updatingTask = taskRepository.findById(newTask.getId()).orElse(null);
        assertNotNull(updatingTask);

        updatingTask.setDescription("Descripción Nueva");
        Task updatedTask = taskRepository.save(updatingTask);

        assertEquals("Descripción Nueva", updatedTask.getDescription());
    }

    @Test
    void shouldFindAndChangeTaskStatus() {
        Task newTask = new Task("T1", "Desc", LocalDateTime.now().plusDays(5), PriorityEnum.HIGH, StatusEnum.TO_DO);
        taskRepository.save(newTask);

        Task updatingTask = taskRepository.findById(newTask.getId()).orElse(null);
        assertNotNull(updatingTask);

        updatingTask.setStatus(StatusEnum.IN_PROGRESS);
        Task updatedTask = taskRepository.save(updatingTask);

        assertEquals(StatusEnum.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("Tarea X", "Desc", LocalDateTime.now(), PriorityEnum.LOW, StatusEnum.TO_DO);
        taskRepository.save(task);

        taskRepository.deleteById(task.getId());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    private static Task createTaskEntity() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Descripción");
        task.setDueDateTime(LocalDateTime.now().plusDays(1));
        task.setCreatedAt(LocalDateTime.now().plusDays(1));
        task.setUpdateAt(LocalDateTime.now().plusDays(1));
        task.setPriority(PriorityEnum.HIGH);
        task.setStatus(StatusEnum.TO_DO);
        return task;
    }

    private static void assertResult(Task expectedResult, Task actualResult) {
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getTitle(), actualResult.getTitle());
        assertEquals(expectedResult.getDescription(), actualResult.getDescription());
        assertEquals(expectedResult.getCreatedAt(), actualResult.getCreatedAt());
        assertEquals(expectedResult.getDueDateTime(), actualResult.getDueDateTime());
        assertEquals(expectedResult.getUpdateAt(), actualResult.getUpdateAt());
        assertEquals(expectedResult.getPriority(), actualResult.getPriority());
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }
}
