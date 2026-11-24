package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.CreateOrderProductRequestDto;
import com.example.FakeCommerce.dtos.OrderProductResponseDto;
import com.example.FakeCommerce.dtos.UpdateOrderProductRequestDto;
import com.example.FakeCommerce.services.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-products")
@RequiredArgsConstructor
public class OrderProductsController {
    private final OrderProductService orderProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderProductResponseDto createOrderProduct(@RequestBody CreateOrderProductRequestDto request) {
        return orderProductService.createOrderProduct(request);
    }

    @GetMapping
    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProductService.getOrderProducts();
    }

    @GetMapping("/order/{orderId}")
    public List<OrderProductResponseDto> getOrderProductsByOrderId(@PathVariable Long orderId) {
        return orderProductService.getOrderProductsByOrderId(orderId);
    }

    @GetMapping("/{id}")
    public OrderProductResponseDto getOrderProductById(@PathVariable Long id) {
        return orderProductService.getOrderProductById(id);
    }

    @PutMapping("/{id}")
    public OrderProductResponseDto updateOrderProduct(@PathVariable Long id,
                                                      @RequestBody UpdateOrderProductRequestDto request) {
        return orderProductService.updateOrderProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable Long id) {
        orderProductService.deleteOrderProduct(id);
        return ResponseEntity.noContent().build();
    }
}
