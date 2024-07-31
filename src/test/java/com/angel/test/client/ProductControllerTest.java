package com.angel.test.client;

import com.angel.test.controller.ProductController;
import com.angel.test.dao.ProductRepository;
import com.angel.test.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use MockBean here to allow Spring to create a mock instance
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setPrice(19.99);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()));
    }

    @Test
    public void testGetProductByIdFound() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productRepository.save(any())).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sample Product\", \"price\":19.99}")) // Include necessary fields
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\", \"price\":29.99}")) // Include fields for update
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(29.99));
    }

    @Test
    public void testUpdateProductNotFound() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\", \"price\":29.99}")) // Include the necessary fields
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProductNotFound() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}