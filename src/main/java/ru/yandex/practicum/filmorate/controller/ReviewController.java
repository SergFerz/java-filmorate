package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@ControllerAdvice
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/reviews")
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@Valid @PathVariable long id) {
        reviewService.deleteReviewById(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReview(@Valid @PathVariable long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews(@RequestParam(name = "filmId", required = false, defaultValue = "0") long filmId,
                                      @RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        return reviewService.getAllReviews(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public Review createLike(@Valid @PathVariable long id, @Valid @PathVariable long userId) {
        return reviewService.createLike(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public Review createDislike(@Valid @PathVariable long id, @Valid @PathVariable long userId) {
        return reviewService.createDislike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public Review deleteLike(@Valid @PathVariable long id, @Valid @PathVariable long userId) {
        return reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public Review deleteDislike(@Valid @PathVariable long id, @Valid @PathVariable long userId) {
        return reviewService.deleteDislike(id, userId);
    }
}
