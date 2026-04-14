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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"무선 키보드\",\"category\":\"전자기기\",\"price\":45000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("무선 키보드"))
                .andExpect(jsonPath("$.category").value("전자기기"))
                .andExpect(jsonPath("$.price").value(45000));
    }

    @Test
    void listProducts() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"마우스\",\"category\":\"전자기기\",\"price\":25000}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductNotFound() throws Exception {
        mockMvc.perform(get("/products/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not found"));
    }

    @Test
    void updateProduct() throws Exception {
        String result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"구형 모니터\",\"category\":\"전자기기\",\"price\":200000}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(result, "$.id").toString();

        mockMvc.perform(put("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"신형 모니터\",\"category\":\"전자기기\",\"price\":350000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("신형 모니터"))
                .andExpect(jsonPath("$.price").value(350000));
    }

    @Test
    void deleteProduct() throws Exception {
        String result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"삭제용 상품\",\"category\":\"기타\",\"price\":10000}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(result, "$.id").toString();

        mockMvc.perform(delete("/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    void searchProducts() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"블루투스 스피커\",\"category\":\"음향기기\",\"price\":89000}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/products/search").param("q", "블루투스"))
                .andExpect(status().isOk());
    }

    @Test
    void createProductValidationBlankName() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"category\":\"전자기기\",\"price\":10000}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProductValidationNegativePrice() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"상품\",\"category\":\"기타\",\"price\":-100}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProductValidationMissingCategory() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"상품\",\"price\":10000}"))
                .andExpect(status().isBadRequest());
    }
}
