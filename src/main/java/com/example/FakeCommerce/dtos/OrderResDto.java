package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.Schema.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResDto {
    private Long id;
    private OrderStatus orderStatus;
    private List<OrderProductResponseDto> items;
    private LocalDateTime createdAt;
}
