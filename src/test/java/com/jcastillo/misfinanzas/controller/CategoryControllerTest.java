package com.jcastillo.misfinanzas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcastillo.misfinanzas.dto.Category.CategoryRequest;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // O @MockBean según la versión
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class) // Solo carga CategoryController en el contexto
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc; // Nuestro "Postman" automatizado

    private final ObjectMapper objectMapper = new ObjectMapper(); // Para convertir objetos Java a String JSON

    @MockitoBean // En Spring Boot 3.4+ / 4.x se usa @MockitoBean (reemplaza al viejo @MockBean)
    private CategoryService categoryService; // Simulamos el servicio

    @Test
    void findAll_ShouldReturnListOfCategories() throws Exception {
        // Arrange
        Category cat = new Category("Salario", CategoryType.INCOME, "#00FF00");
        cat.setId(1L);
        when(categoryService.findAll()).thenReturn(List.of(cat));

        // Act & Assert
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Salario"))
                .andExpect(jsonPath("$[0].type").value("INCOME"));
    }

    @Test
    void create_WhenValidRequest_ShouldReturn201() throws Exception {
        // Arrange
        CategoryRequest request = new CategoryRequest("Comida", CategoryType.EXPENSE, "#FF0000");
        Category saved = new Category("Comida", CategoryType.EXPENSE, "#FF0000");
        saved.setId(1L);

        when(categoryService.save(any(Category.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convertimos el DTO a JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Comida"));
    }

    @Test
    void create_WhenInvalidRequest_ShouldReturn400() throws Exception {
        // Arrange: Enviamos un request inválido (nombre en blanco y type null)
        CategoryRequest invalidRequest = new CategoryRequest("", null, "#FF0000");

        // Act & Assert: Debe fallar en la validación @Valid del controlador
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }
}