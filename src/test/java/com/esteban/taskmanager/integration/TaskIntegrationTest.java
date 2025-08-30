package com.esteban.taskmanager.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndRetrieveTask() throws Exception {
        String json = """
        {
          "title": "Tarea1",
          "description": "descripcion",
          "dueDate": "01-01-2030T10:00",
          "priority": "LOW"
        }
        """;

        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.taskResponseList[0].title").value("Tarea1"));

    }
}
