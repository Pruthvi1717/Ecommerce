package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.OrderReqDto;
import com.example.FakeCommerce.dtos.OrderResDto;
import com.example.FakeCommerce.dtos.OrderStatusReqDto;
import com.example.FakeCommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResDto createOrder(@RequestBody OrderReqDto request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderResDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PatchMapping("/{id}/status")
    public OrderResDto updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusReqDto request) {
        return orderService.updateOrderStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
