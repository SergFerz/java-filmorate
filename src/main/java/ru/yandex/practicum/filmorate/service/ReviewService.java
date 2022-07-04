package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewDao reviewDao;
    private final UserService userService;
    private final FilmService filmService;

    public Review createReview(Review review) {
        validateReview(review);
        return reviewDao.createReview(review);
    }

    public Review updateReview(Review review) {
        getReviewById(review.getId());
        return reviewDao.updateReview(review);
    }

    public void deleteReviewById(long id) {
        getReviewById(id);
        reviewDao.deleteReview(id);
    }

    public Review getReviewById(long id) {
        Review review = reviewDao.getReviewById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return review;
    }

    public List<Review> getAllReviews(long filmId, int count) {
        List<Review> reviewList = new ArrayList<>();
        if (filmId == 0) {
            reviewList = reviewDao.getAllReviews();
        } else {
            reviewList = reviewDao.getReviewsByFilmID(filmId);
        }
        if (reviewList.size() > count) {
            reviewList = reviewList.subList(0, count);
        }
        reviewList.sort(Comparator.comparing(Review::getUseful).reversed());
        return reviewList;
    }

    public Review createLike(long id, long userId) {
        userService.getUserById(userId);
        getReviewById(id);
        reviewDao.createLike(id, userId);
        Review review = getReviewById(id);
        return review;
    }

    public Review createDislike(long id, long userId) {
        userService.getUserById(userId);
        getReviewById(id);
        reviewDao.createDislike(id, userId);
        Review review = getReviewById(id);
        return review;
    }

    public Review deleteLike(long id, long userId) {
        userService.getUserById(userId);
        getReviewById(id);
        reviewDao.deleteLike(id,userId);
        Review review = getReviewById(id);
        return review;
    }

    public Review deleteDislike(long id, long userId) {
        userService.getUserById(userId);
        getReviewById(id);
        reviewDao.deleteDislike(id,userId);
        Review review = getReviewById(id);
        return review;
    }

    private Review validateReview(Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        if (review.getContent() == null) {
            throw new ValidationException("Введено некорректное значение content");
        } else if (review.getIsPositive() == null) {
            throw new ValidationException("Введено некорректное значение IsPositive");
        } else
            return review;
    }
}
