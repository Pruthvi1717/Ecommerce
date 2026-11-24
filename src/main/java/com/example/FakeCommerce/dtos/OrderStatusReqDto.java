package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.Schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusReqDto {
    private OrderStatus orderStatus;
}