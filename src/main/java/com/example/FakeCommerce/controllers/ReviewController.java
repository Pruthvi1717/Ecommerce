package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.CreateReviewRequestDto;
import com.example.FakeCommerce.dtos.CreateReviewResponseDto;
import com.example.FakeCommerce.dtos.UpdateReviewRequestDto;
import com.example.FakeCommerce.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public CreateReviewResponseDto giveReviewOnProduct(@RequestBody CreateReviewRequestDto reqDto){
        return reviewService.createReview(reqDto);
    }

    @GetMapping("/{id}")
    public CreateReviewResponseDto getReviewUsingId(@PathVariable Long id){
        return reviewService.getReviewById(id);
    }

    @GetMapping("/product/{productId}")
    public List<CreateReviewResponseDto> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PutMapping("/{id}")
    public CreateReviewResponseDto updateReview(@PathVariable Long id,
                                                @RequestBody UpdateReviewRequestDto reqDto) {
        return reviewService.updateReview(id, reqDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
