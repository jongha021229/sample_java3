package com.example.samplejava3.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createCourse() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Spring Boot Basics\",\"fee\":99.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Spring Boot Basics"))
                .andExpect(jsonPath("$.fee").value(99.0));
    }

    @Test
    void listCourses() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Java Core\",\"fee\":49.0}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseNotFound() throws Exception {
        mockMvc.perform(get("/courses/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not found"));
    }

    @Test
    void searchCourses() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Docker for Beginners\",\"fee\":59.0}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/courses/search").param("q", "docker"))
                .andExpect(status().isOk());
    }

    @Test
    void createCourseValidation() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"fee\":-1}"))
                .andExpect(status().isBadRequest());
    }
}
