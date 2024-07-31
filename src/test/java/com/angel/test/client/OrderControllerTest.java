package com.angel.test.client;

import com.angel.test.controller.OrderController;
import com.angel.test.dao.OrderRepository;
import com.angel.test.entity.Order;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use MockBean here
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
        // Set other properties if needed
    }

    @Test
    public void testGetAllOrders() throws Exception {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(order.getId()));
    }

    @Test
    public void testGetOrderByIdFound() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    public void testGetOrderByIdNotFound() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(orderRepository.save(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include necessary fields according to your Order structure
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        mockMvc.perform(put("/api/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include fields for update
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    public void testUpdateOrderNotFound() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include the necessary fields
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(delete("/api/orders/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOrderNotFound() throws Exception {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/orders/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}