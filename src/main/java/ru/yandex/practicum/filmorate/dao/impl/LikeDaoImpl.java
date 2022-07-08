package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Like addLike(long idFilm, long idUser) {
        Like like = new Like(idFilm, idUser);
        jdbcTemplate.update("INSERT INTO likes(film_id, user_id) VALUES (?, ?);", idFilm, idUser);
        return like;
    }

    @Override
    public int deleteLike(long idFilm, long idUser) {
        return jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", idFilm, idUser);
    }

    @Override
    public Map<Long, Set<Long>> getLikesForAllFilms() {
        Map<Long, Set<Long>> likes = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM likes", rs -> {
            Long filmId = rs.getLong("film_id");
            Set<Long> filmLikes = likes.getOrDefault(filmId, new HashSet<>());
            filmLikes.add(rs.getLong("user_id"));
            likes.put(filmId, filmLikes);
        });
        return likes;
    }

    @Override
    public Map<Long, Map<Long, Double>> buildDifferencesMatrix() {
        Map<Long, Map<Long, Double>> data = new HashMap<>();
        HashMap<Long, Double> films = new HashMap<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes");
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id FROM likes group by film_id");
        while (filmRows.next()) {
            films.put(filmRows.getLong("film_id"), 0.0);
        }
        while (likeRows.next()) {
            if (!data.containsKey(likeRows.getLong("user_id"))) {
                data.put(likeRows.getLong("user_id"), new HashMap<>(films));
            }
            data.get(likeRows.getLong("user_id")).put(likeRows.getLong("film_id"), 1.0);
        }
        return data;
    }

    @Override
    public Set<Long> getLikesByFilmId(long filmId) {
        Set<Long> likes = new HashSet<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE film_id=?",
                filmId);
        if (likeRows.next()) {
            likes.add(likeRows.getLong("user_id"));
        }
        return likes;
    }
}
