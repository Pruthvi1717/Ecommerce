package com.example.FakeCommerce.services;
import com.example.FakeCommerce.Exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.Schema.Category;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.dtos.GetProductResponseDto;
import com.example.FakeCommerce.dtos.createProductRequestDto;
import com.example.FakeCommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    public List<GetProductResponseDto> getAllProducts(){

        List<Product> products = productRepository.findAll();

        List<GetProductResponseDto> response = new ArrayList<>();
        for(Product product: products){
            GetProductResponseDto resDto = GetProductResponseDto.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .image(product.getImage())
                    .rating(product.getRating())
                    .build();
            response.add(resDto);
        }
        return response;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public GetProductResponseDto createProduct(createProductRequestDto reqDto){
        Category category = categoryService.getCategoryById(
                reqDto.getCategoryId()
        );
        Product newProduct = Product.builder()
                .title(reqDto.getName())
                .description(reqDto.getDescription())
                .image(reqDto.getImage())
                .price(reqDto.getPrice())
                .category(category)
                .rating(reqDto.getRating())
                .build();

        Product p1 = productRepository.save(newProduct);

        GetProductResponseDto dto = GetProductResponseDto.builder()
                .id(p1.getId())
                .title(p1.getTitle())
                .description(p1.getDescription())
                .image(reqDto.getImage())
                .price(reqDto.getPrice())
                .rating(reqDto.getRating())
                .build();

        return dto;


    }

    public void deleteProducts(Long id){
        productRepository.deleteById(id);
    }

    public List<Product> getProductByCategory(String category){
        return productRepository.findByCategory(category);
    }

    public List<String> getCategories(){
        return productRepository.findAllCategories();
    }
}
