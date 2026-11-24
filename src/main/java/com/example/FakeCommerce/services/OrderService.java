package com.example.FakeCommerce.services;

import com.example.FakeCommerce.Exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.Schema.Order;
import com.example.FakeCommerce.Schema.OrderProducts;
import com.example.FakeCommerce.Schema.OrderStatus;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.dtos.OrderProductRequestDto;
import com.example.FakeCommerce.dtos.OrderProductResponseDto;
import com.example.FakeCommerce.dtos.OrderReqDto;
import com.example.FakeCommerce.dtos.OrderResDto;
import com.example.FakeCommerce.dtos.OrderStatusReqDto;
import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;

    @Transactional
    public OrderResDto createOrder(OrderReqDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Order request is required");
        }

        List<OrderProductRequestDto> items = getRequestedItems(request);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one product");
        }
        items.forEach(this::validateOrderItem);

        Order order = orderRepository.save(Order.builder()
                .orderStatus(request.getOrderStatus() == null ? OrderStatus.PENDING : request.getOrderStatus())
                .build());

        List<OrderProducts> orderProducts = new ArrayList<>();
        for (OrderProductRequestDto item : items) {
            Product product = productService.getProductById(item.getProductId());
            orderProducts.add(OrderProducts.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .build());
        }
        orderProductRepository.saveAll(orderProducts);
        return toResponseDto(order, orderProducts);
    }

    @Transactional(readOnly = true)
    public List<OrderResDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResDto getOrderById(Long id) {
        return toResponseDto(findOrderById(id));
    }

    @Transactional
    public OrderResDto updateOrderStatus(Long id, OrderStatusReqDto request) {
        if (request == null || request.getOrderStatus() == null) {
            throw new IllegalArgumentException("Order status is required");
        }
        Order order = findOrderById(id);
        order.setOrderStatus(request.getOrderStatus());
        return toResponseDto(orderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = findOrderById(id);
        orderProductRepository.deleteAll(orderProductRepository.findByOrder_Id(id));
        orderRepository.delete(order);
    }

    private List<OrderProductRequestDto> getRequestedItems(OrderReqDto request) {
        if (request.getItems() != null && request.getProduct_ids() != null) {
            throw new IllegalArgumentException("Provide either items or product_ids, not both");
        }
        if (request.getItems() != null) {
            return request.getItems();
        }
        if (request.getProduct_ids() == null) {
            return List.of();
        }
        return request.getProduct_ids().stream()
                .map(productId -> OrderProductRequestDto.builder().productId(productId).quantity(1).build())
                .toList();
    }

    private void validateOrderItem(OrderProductRequestDto item) {
        if (item == null || item.getProductId() == null || item.getProductId() <= 0) {
            throw new IllegalArgumentException("Every order item must have a valid productId");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order item quantity must be greater than zero");
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderResDto toResponseDto(Order order) {
        return toResponseDto(order, orderProductRepository.findByOrder_Id(order.getId()));
    }

    private OrderResDto toResponseDto(Order order, List<OrderProducts> items) {
        return OrderResDto.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .items(items.stream().map(this::toItemResponseDto).toList())
                .build();
    }

    private OrderProductResponseDto toItemResponseDto(OrderProducts item) {
        return OrderProductResponseDto.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .productId(item.getProduct().getId())
                .productTitle(item.getProduct().getTitle())
                .quantity(item.getQuantity())
                .build();
    }
}
