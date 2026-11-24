package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.Schema.Category;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class createProductRequestDto {
     private String name;
     private String description;
     private String image;
     private BigDecimal price;
     private Long categoryId;
     @Column(precision = 3, scale = 2)
     private BigDecimal rating;
}
