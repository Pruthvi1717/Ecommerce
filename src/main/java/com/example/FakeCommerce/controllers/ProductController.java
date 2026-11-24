package com.example.FakeCommerce.controllers;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.createProductRequestDto;
import com.example.FakeCommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public List<GetProductResponseDto> getAllProducts(){

        return productService.getAllProducts();
    }

    @PostMapping
    public GetProductResponseDto createProduct(@RequestBody createProductRequestDto requestDto){
        return productService.createProduct(requestDto);
    }

    @DeleteMapping("/{id}")
    public void delProduct(@PathVariable Long id){
        productService.deleteProducts(id);
    }

    @GetMapping("/search")
    public List<Product> getProByCat(@RequestParam("category") String category){
        return productService.getProductByCategory(category);
    }

    @GetMapping("/all")
    public List<String> getAllCat(){
        return productService.getCategories();
    }
}
