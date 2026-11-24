package com.example.FakeCommerce.services;

import com.example.FakeCommerce.Exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.Schema.Product;
import com.example.FakeCommerce.Schema.Review;
import com.example.FakeCommerce.dtos.CreateReviewRequestDto;
import com.example.FakeCommerce.dtos.CreateReviewResponseDto;
import com.example.FakeCommerce.dtos.UpdateReviewRequestDto;
import com.example.FakeCommerce.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    public CreateReviewResponseDto createReview(CreateReviewRequestDto reqDto){
        Product p = productService.getProductById(reqDto.getProduct_id());
        Review review = Review.builder()
                .content(reqDto.getContent())
                .product(p)
                .build();
        return toResponseDto(reviewRepository.save(review));
    }

    public CreateReviewResponseDto getReviewById(Long id){
        return toResponseDto(findReviewById(id));
    }

    public List<CreateReviewResponseDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProduct_Id(productId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public CreateReviewResponseDto updateReview(Long id, UpdateReviewRequestDto reqDto) {
        Review review = findReviewById(id);
        review.setContent(reqDto.getContent());
        return toResponseDto(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        reviewRepository.delete(findReviewById(id));
    }

    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    private CreateReviewResponseDto toResponseDto(Review review) {
        return CreateReviewResponseDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .product_id(review.getProduct().getId())
                .build();
    }
}
