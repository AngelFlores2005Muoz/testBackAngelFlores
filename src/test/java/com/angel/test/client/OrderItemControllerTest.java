package com.angel.test.client;

import com.angel.test.controller.OrderItemController;
import com.angel.test.dao.OrderItemRepository;
import com.angel.test.entity.OrderItem;
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

@WebMvcTest(OrderItemController.class)
public class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use MockBean here to allow Spring to create a mock instance
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemController orderItemController;

    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItem = new OrderItem();
        orderItem.setId(1L);
        // Set other properties as needed, e.g., order and product relationships
    }

    @Test
    public void testGetAllOrderItems() throws Exception {
        when(orderItemRepository.findAll()).thenReturn(Arrays.asList(orderItem));

        mockMvc.perform(get("/api/order-items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(orderItem.getId()));
    }

    @Test
    public void testGetOrderItemByIdFound() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));

        mockMvc.perform(get("/api/order-items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItem.getId()));
    }

    @Test
    public void testGetOrderItemByIdNotFound() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/order-items/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrderItem() throws Exception {
        when(orderItemRepository.save(any())).thenReturn(orderItem);

        mockMvc.perform(post("/api/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include necessary fields for the OrderItem JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItem.getId()));
    }

    @Test
    public void testUpdateOrderItem() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any())).thenReturn(orderItem);

        mockMvc.perform(put("/api/order-items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include necessary fields for the OrderItem update
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItem.getId()));
    }

    @Test
    public void testUpdateOrderItemNotFound() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/order-items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Include necessary fields
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrderItem() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));

        mockMvc.perform(delete("/api/order-items/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOrderItemNotFound() throws Exception {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/order-items/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}