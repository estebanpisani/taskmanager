package com.esteban.taskmanager.integration;

import com.esteban.taskmanager.dao.TaskRepository;
import com.esteban.taskmanager.domain.Task;
import com.esteban.taskmanager.domain.enums.PriorityEnum;
import com.esteban.taskmanager.domain.enums.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.TO_DO);
        Task task2 = new Task("T2", "Desc2", LocalDateTime.now().plusDays(2),
                PriorityEnum.MEDIUM, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.taskResponseList.length()").value(2))
                .andExpect(jsonPath("$._embedded.taskResponseList[0].title").value("T1"))
                .andExpect(jsonPath("$._embedded.taskResponseList[1].title").value("T2"));

    }

    @Test
    void shouldNotGetTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.TO_DO);
        Task task2 = new Task("T2", "Desc2", LocalDateTime.now().plusDays(2),
                PriorityEnum.MEDIUM, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("T1"))
                .andExpect(jsonPath("$.description").value("Desc1"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TO_DO"));

    }

    @Test
    void shouldNotGetTask() throws Exception {
        mockMvc.perform(get("/tasks/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("TASK_NOT_FOUND"));
    }

    @Test
    void shouldCreateAndRetrieveTask() throws Exception {
        String json = """
        {
          "title": "Tarea1",
          "description": "descripcion",
          "dueDate": "01-01-2230T10:00",
          "priority": "LOW"
        }
        """;

        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.taskResponseList[0].title").value("Tarea1"))
                .andExpect(jsonPath("$._embedded.taskResponseList[0].description").value("descripcion"))
                .andExpect(jsonPath("$._embedded.taskResponseList[0].dueDate").value("01-01-2230T10:00"))
                .andExpect(jsonPath("$._embedded.taskResponseList[0].priority").value("LOW"));

    }

    @Test
    void shouldNotCreateTaskWithNullTitle() throws Exception {
        String json = """
        {
          "title": "",
          "description": "descripcion",
          "dueDate": "01-01-2201T10:00",
          "priority": "LOW"
        }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parámetros inválidos"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETERS"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("La tarea debe tener un título."));

    }

    @Test
    void shouldNotCreateTaskWithInvalidDate() throws Exception {
        String json = """
        {
          "title": "Tarea1",
          "description": "descripcion",
          "dueDate": "01-01-2001T10:00",
          "priority": "LOW"
        }
        """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parámetros inválidos"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETERS"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("dueDate"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("La fecha ingresada ya pasó."));

    }

    @Test
    void shouldNotCreateTaskWithInvalidDescription() throws Exception {
        String invalidDescription = "A".repeat(256);
        String json = String.format("""
        {
          "title": "Tarea1",
          "description":"%s",
          "dueDate": "01-01-2201T10:00",
          "priority": "LOW"
        }
        """, invalidDescription);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parámetros inválidos"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETERS"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("description"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("La descripción no puede superar los 255 caracteres."));

    }

    @Test
    void shouldUpdateTask() throws Exception {
        String json = """
        {
          "title": "Tarea1",
          "description": "descripcion",
          "dueDate": "01-01-2030T10:00",
          "priority": "LOW"
        }
        """;

        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.TO_DO);
        Task task2 = new Task("T2", "Desc2", LocalDateTime.now().plusDays(2),
                PriorityEnum.MEDIUM, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(put("/tasks/{id}", task1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tarea1"))
                .andExpect(jsonPath("$.description").value("descripcion"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.status").value("TO_DO"));

    }

    @Test
    void shouldStartTask() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.TO_DO);

        taskRepository.save(task1);

        mockMvc.perform(patch("/tasks/{id}/start", task1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldDoneTask() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);

        mockMvc.perform(patch("/tasks/{id}/done", task1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));

    }

    @Test
    void shouldResetTask() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);

        mockMvc.perform(patch("/tasks/{id}/reset", task1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TO_DO"));

    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task1 = new Task("T1", "Desc1", LocalDateTime.now().plusDays(1),
                PriorityEnum.HIGH, StatusEnum.IN_PROGRESS);

        taskRepository.save(task1);

        mockMvc.perform(delete("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontró la tarea con id: " + task1.getId()))
                .andExpect(jsonPath("$.errorCode").value("TASK_NOT_FOUND"));
    }

}
