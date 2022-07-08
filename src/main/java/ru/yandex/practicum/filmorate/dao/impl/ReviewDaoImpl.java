package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Review createReview(Review review) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("reviews").usingGeneratedKeyColumns("review_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", review.getUserId())
                .addValue("film_id", review.getFilmId())
                .addValue("content", review.getContent())
                .addValue("is_positive", review.getIsPositive())
                .addValue("useful", review.getUseful());
        Number num = jdbcInsert.executeAndReturnKey(parameters);
        if (getReviewById(num.longValue()).isPresent()) {
            return getReviewById(num.longValue()).get();
        } else return null;
    }

    public Review updateReview(Review review) {
        jdbcTemplate.update("UPDATE reviews SET content=?, is_positive=?, useful=? WHERE review_id=?",
                review.getContent(),
                review.getIsPositive(),
                review.getUseful(),
                review.getId());
        return getReviewById(review.getId()).get();
    }

    public void deleteReview(long id) {
        jdbcTemplate.update("DELETE FROM review_likes WHERE review_id=?", id);
        jdbcTemplate.update("DELETE FROM reviews WHERE review_id=?", id);
    }

    public Optional<Review> getReviewById(long id) {
        SqlRowSet reviewsRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE review_id = ?", id);
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM review_likes WHERE review_id=?", id);
        int useful = 0;
        while (likesRows.next()) {
            useful = useful + likesRows.getInt("score");
        }
        if (reviewsRows.next()) {
            Review review = new Review(
                    reviewsRows.getLong("review_id"),
                    reviewsRows.getLong("user_id"),
                    reviewsRows.getLong("film_id"),
                    reviewsRows.getString("content"),
                    reviewsRows.getBoolean("is_positive"),
                    useful
            );
            log.info("Найдено ревью: {} {}", review.getId(), review.getContent());
            return Optional.of(review);
        } else {
            log.info("Ревью с идентификатором {} не найдено.", id);
            return Optional.empty();
        }
    }

    public List<Review> getAllReviews() {
        List<Review> reviewList = new ArrayList<>();
        SqlRowSet reviewsIdRows = jdbcTemplate.queryForRowSet("SELECT review_id FROM reviews");
        while (reviewsIdRows.next()) {
            if (getReviewById(reviewsIdRows.getInt("review_id")).isPresent()) {
                reviewList.add(getReviewById(reviewsIdRows.getInt("review_id")).get());
            } else continue;
        }
        return reviewList;
    }

    public List<Review> getReviewsByFilmID(long filmId) {
        List<Review> reviewList = new ArrayList<>();
        SqlRowSet reviewsRows = jdbcTemplate.queryForRowSet("SELECT review_id FROM reviews WHERE film_id=?",
                filmId);
        while (reviewsRows.next()) {
            if (getReviewById(reviewsRows.getInt("review_id")).isPresent()) {
                reviewList.add(getReviewById(reviewsRows.getInt("review_id")).get());
            } else continue;
        }
        return reviewList;
    }

    public void createLike(long id, long userId) {
        jdbcTemplate.update("INSERT INTO review_likes(review_id, user_id, score) VALUES (?,?,?)",
                id, userId, 1);
    }

    public void createDislike(long id, long userId) {
        jdbcTemplate.update("INSERT INTO review_likes(review_id, user_id, score) VALUES (?,?,?)",
                id, userId, -1);
    }

    public void deleteLike(long id, long userId) {
        jdbcTemplate.update("DELETE FROM review_likes WHERE review_id=? AND user_id=?", id, userId);
    }

    public void deleteDislike(long id, long userId) {
        jdbcTemplate.update("DELETE FROM review_likes WHERE review_id=? AND user_id=?", id, userId);
    }
}
