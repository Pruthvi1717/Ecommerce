package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.Schema.Category;
import com.example.FakeCommerce.dtos.CreateCategoryDto;
import com.example.FakeCommerce.services.CategoryService;
import com.example.FakeCommerce.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public List<Category> getCategories(){
        return categoryService.getAllCategories();
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(@RequestBody CreateCategoryDto reqDto){
        Category category = categoryService.createCategory(reqDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(category, "Category created Successfully"));
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void delCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

}
