package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.Schema.OrderStatus;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReqDto {
    private OrderStatus orderStatus;
    private List<Long> product_ids;
    private List<OrderProductRequestDto> items;
}
