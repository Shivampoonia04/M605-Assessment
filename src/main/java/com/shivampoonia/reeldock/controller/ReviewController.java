package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.ReviewRequest;
import com.shivampoonia.reeldock.dto.Views.ReviewView;
import com.shivampoonia.reeldock.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviews;

    public ReviewController(ReviewService reviews) {
        this.reviews = reviews;
    }

    @GetMapping
    public List<ReviewView> list() { return reviews.list(); }

    @GetMapping("/production/{productionId}")
    public List<ReviewView> forProduction(@PathVariable Long productionId) {
        return reviews.forProduction(productionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewView post(@Valid @RequestBody ReviewRequest req) { return reviews.post(req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void erase(@PathVariable Long id) { reviews.erase(id); }
}
