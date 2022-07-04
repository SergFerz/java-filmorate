package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Review;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ReviewService {
    private ReviewDao reviewDao;
    private FilmService filmService;
    private UserService userService;

    @Autowired
    public ReviewService(){
    }
    public Review createReview(Review review) throws NotFoundException{
        validateReview(review);
        return reviewDao.createReview(review);
    }

    public Review updateReview(Review review) throws NotFoundException {
        validateReview(review);
        reviewDao.updateReview(review);
        return review;
    }


    public Review removeReviewById(Long id) throws NotFoundException {
        validateReviewId(id);
        Review review = reviewDao.getReviewById(id);
        reviewDao.deleteReview(id);
        return review;
    }


    public Review getReviewById(Long id) throws NotFoundException {
        validateReviewId(id);
        return reviewDao.getReviewById(id);
    }

    public List<Review> getReviewByFilmId(Long filmId, Long count) {
        return reviewDao.getReviewsByFilmID(filmId)
                .stream()
                .sorted((o1, o2) -> o2.getUseful()- o1.getUseful())
                .collect(Collectors.toList());
    }

    public Collection<Review> findAll() {
        return reviewDao.getAllReviews();
    }


    public Review addLikeFromUser(Long id, Long userId) throws NotFoundException {
        Review review = reviewDao.getReviewById(id);
        review.getLikes().add(userId);
        calculateUseful(review);
        reviewDao.createLike(id, userId);
        return review;
    }


    public Review addDislikeFromUser(Long id, Long userId) throws NotFoundException {
        Review review = reviewDao.getReviewById(id);
        review.getDislikes().add(userId);
        calculateUseful(review);
        reviewDao.createDislike(id, userId);
        return review;
    }

    public Review deleteLikeFromUser(Long id, Long userId){
        Review review = reviewDao.getReviewById(id);
        review.getLikes().remove(userId);
        calculateUseful(review);
        reviewDao.deleteLike(id, userId);
        return review;
    }

    public Review deleteDislikeFromUser(Long id, Long userId)  {
        Review review = reviewDao.getReviewById(id);
        review.getDislikes().remove(userId);
        calculateUseful(review);
        reviewDao.deleteDislike(id, userId);
        return review;
    }


    private void validateReview(Review review) throws NotFoundException {
        if (review.getReviewId() != null) {
            validateReviewId(review.getReviewId());
        }
        if (filmService.getFilmById(review.getFilmId()) == null) {
            log.warn("film with ID not found");
            throw new NotFoundException("film with ID not found");
        }
        if (userService.getUserById(review.getUserId()) == null) {
            log.warn("user with ID not found");
            throw new NotFoundException("user with ID not found");
        }
    }

    private void validateReviewId(Long id) throws NotFoundException {
        if (id <= 0 || !reviewDao.getAllReviews().contains(id)) {
            log.warn("review with ID not found");
            throw new NotFoundException("review with ID not found");
        }
    }

    private void calculateUseful(Review review) {
        int defaultUseful = reviewDao.getReviewById(review.getReviewId()).getUseful();
        if (Objects.nonNull(review.getLikes()) && !review.getLikes().isEmpty()) {
            review.setUseful(defaultUseful + review.getLikes().size());
        } else if (Objects.nonNull(review.getDislikes()) && !review.getDislikes().isEmpty()) {
            review.setUseful(defaultUseful - review.getDislikes().size());
        } else {
            review.setUseful(defaultUseful);
        }
    }
}