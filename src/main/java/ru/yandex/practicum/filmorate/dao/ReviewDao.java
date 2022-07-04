package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long id);

    Optional<Review> getReviewById(long id);

    List<Review> getAllReviews();

    List<Review> getReviewsByFilmID(long filmId);

    void createLike(long id, long userId);

    void createDislike(long id, long userId);

    void deleteLike(long id, long userId);

    void deleteDislike(long id, long userId);
}
