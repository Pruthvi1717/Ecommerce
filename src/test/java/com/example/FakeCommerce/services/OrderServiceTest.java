package com.example.FakeCommerce.services;

import com.example.FakeCommerce.Schema.Order;
import com.example.FakeCommerce.Schema.OrderProducts;
import com.example.FakeCommerce.Schema.OrderStatus;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.dtos.OrderProductRequestDto;
import com.example.FakeCommerce.dtos.OrderReqDto;
import com.example.FakeCommerce.dtos.OrderResDto;
import com.example.FakeCommerce.repositories.OrderProductRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private OrderRepository orderRepository;
    private OrderProductRepository orderProductRepository;
    private ProductService productService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderProductRepository = mock(OrderProductRepository.class);
        productService = mock(ProductService.class);
        orderService = new OrderService(orderRepository, orderProductRepository, productService);
    }

    @Test
    void createOrderPersistsProductsAndDefaultsStatusToPending() {
        Product product = Product.builder().title("Keyboard").build();
        product.setId(7L);
        when(productService.getProductById(7L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(10L);
            order.setCreatedAt(LocalDateTime.now());
            return order;
        });
        when(orderProductRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<OrderProducts> items = invocation.getArgument(0);
            items.get(0).setId(15L);
            return items;
        });

        OrderReqDto request = OrderReqDto.builder()
                .items(List.of(OrderProductRequestDto.builder().productId(7L).quantity(2).build()))
                .build();

        OrderResDto response = orderService.createOrder(request);

        assertEquals(10L, response.getId());
        assertEquals(OrderStatus.PENDING, response.getOrderStatus());
        assertEquals(1, response.getItems().size());
        assertEquals(7L, response.getItems().get(0).getProductId());
        assertEquals("Keyboard", response.getItems().get(0).getProductTitle());
        assertEquals(2, response.getItems().get(0).getQuantity());
        verify(orderProductRepository).saveAll(anyList());
    }

    @Test
    void createOrderRejectsInvalidQuantityBeforeSavingOrder() {
        OrderReqDto request = OrderReqDto.builder()
                .items(List.of(OrderProductRequestDto.builder().productId(7L).quantity(0).build()))
                .build();

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderProductRepository, never()).saveAll(anyList());
    }
}