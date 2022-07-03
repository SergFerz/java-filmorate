package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;


@Service
public interface ReviewDao {
    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long id);

    Review getReviewById(long id);

    List<Review> getAllReviews();

    List<Review> getReviewsByFilmID(long filmId);

    void createLike(long id, long userId);

    void createDislike(long id, long userId);

    void deleteLike(long id, long userId);

    void deleteDislike(long id, long userId);
}