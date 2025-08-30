package com.esteban.taskmanager.repository;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

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
