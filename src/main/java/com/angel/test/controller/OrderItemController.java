package com.angel.test.controller;

import com.angel.test.dao.OrderItemRepository;
import com.angel.test.entity.OrderItem;
import com.angel.test.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemRepository.findById(id)
                .map(orderItem -> ResponseEntity.ok(orderItem))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItemDetails) {
        return orderItemRepository.findById(id)
                .map(orderItem -> {
                    orderItem.setOrder(orderItemDetails.getOrder());
                    orderItem.setProduct(orderItemDetails.getProduct());
                    orderItem.setQuantity(orderItemDetails.getQuantity());
                    OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
                    return ResponseEntity.ok(updatedOrderItem);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        orderItemRepository.delete(orderItem);
        return ResponseEntity.ok().build();
    }
}
