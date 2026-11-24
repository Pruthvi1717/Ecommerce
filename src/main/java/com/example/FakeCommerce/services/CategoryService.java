package com.example.FakeCommerce.services;

import com.example.FakeCommerce.Exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.Schema.Category;
import com.example.FakeCommerce.dtos.CreateCategoryDto;
import com.example.FakeCommerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
   private final CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryDto reqDto) {
        Category c = Category.builder()
                .name(reqDto.getName())
                .build();

        return categoryRepository.save(c);
    }

    public List<Category> getAllCategories(){

        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }


}
