package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.Schema.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProducts, Long> {
    List<OrderProducts> findByOrder_Id(Long orderId);
}
