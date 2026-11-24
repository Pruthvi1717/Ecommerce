package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.Schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    @Query(nativeQuery = true, value= "SELECT DISTINCT category FROM products")
    List<String> findAllCategories();
}
