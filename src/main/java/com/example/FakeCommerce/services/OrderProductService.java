package com.example.FakeCommerce.services;

import com.example.FakeCommerce.Exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.Schema.Order;
import com.example.FakeCommerce.Schema.OrderProducts;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.dtos.CreateOrderProductRequestDto;
import com.example.FakeCommerce.dtos.OrderProductResponseDto;
import com.example.FakeCommerce.dtos.UpdateOrderProductRequestDto;
import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderProductResponseDto createOrderProduct(CreateOrderProductRequestDto request) {
        if (request == null || request.getOrderId() == null || request.getOrderId() <= 0) {
            throw new IllegalArgumentException("A valid orderId is required");
        }
        if (request.getProductId() == null || request.getProductId() <= 0) {
            throw new IllegalArgumentException("A valid productId is required");
        }
        validateQuantity(request.getQuantity());

        Order order = findOrderById(request.getOrderId());
        Product product = productService.getProductById(request.getProductId());
        OrderProducts orderProduct = orderProductRepository.save(OrderProducts.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .build());
        return toResponseDto(orderProduct);
    }

    @Transactional(readOnly = true)
    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProductRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderProductResponseDto> getOrderProductsByOrderId(Long orderId) {
        findOrderById(orderId);
        return orderProductRepository.findByOrder_Id(orderId).stream().map(this::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public OrderProductResponseDto getOrderProductById(Long id) {
        return toResponseDto(findOrderProductById(id));
    }

    @Transactional
    public OrderProductResponseDto updateOrderProduct(Long id, UpdateOrderProductRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Order product request is required");
        }
        validateQuantity(request.getQuantity());
        OrderProducts orderProduct = findOrderProductById(id);
        orderProduct.setQuantity(request.getQuantity());
        return toResponseDto(orderProductRepository.save(orderProduct));
    }

    @Transactional
    public void deleteOrderProduct(Long id) {
        orderProductRepository.delete(findOrderProductById(id));
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderProducts findOrderProductById(Long id) {
        return orderProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order product not found with id: " + id));
    }

    private OrderProductResponseDto toResponseDto(OrderProducts item) {
        return OrderProductResponseDto.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .productId(item.getProduct().getId())
                .productTitle(item.getProduct().getTitle())
                .quantity(item.getQuantity())
                .build();
    }
}
