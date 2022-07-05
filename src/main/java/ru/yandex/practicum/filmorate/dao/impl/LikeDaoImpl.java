package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
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

    /**
     * Метод возвращает ассоциативный массив, где ключом является идентификатор фильма, а значением - набор
     * идентификаторов пользователей, поставивших лайки этому фильму. Полученный массив будет содержать только фильмы,
     * удовлетворяющие заданным параметрам genreId, year.
     *
     * @param genreId идентификатор жанра фильма;
     * @param year    год выпуска фильма;
     * @return ассоциативный массив, где ключом является идентификатор фильма, а значением - набор идентификаторов
     * пользователей, поставивших лайки этому фильму
     */
    @Override
    public Map<Long, Set<Long>> getLikesForFilteredFilms(Optional<Integer> genreId, Optional<Integer> year) {
        Map<Long, Set<Long>> likes = new HashMap<>();
        jdbcTemplate.query(getSQLRequestByParameters(genreId, year), rs -> {
            Long filmId = rs.getLong("f_id");
            Set<Long> filmLikes = likes.getOrDefault(filmId, new HashSet<>());
            filmLikes.add(rs.getLong("u_id"));
            likes.put(filmId, filmLikes);
        });
        return likes;
    }

    /**
     * Метод возвращает строку SQL запроса с учетом значения параметров genreId, year, limit.
     *
     * @param genreId   идентификатор жанра;
     * @param year  год выпуска фильма;
     * @return  строка с SQL запросом.
     */
    private String getSQLRequestByParameters(Optional<Integer> genreId, Optional<Integer> year) {
        String sqlRequest;

        if (genreId.isPresent() && year.isEmpty()) {
            sqlRequest = String.format("SELECT l.film_id AS f_id, " +
                                       "       l.user_id AS u_id " +
                                       "FROM likes AS l " +
                                       "WHERE l.film_id=ANY (SELECT film_id " +
                                       "                FROM film_genre " +
                                       "                WHERE id=%s)", genreId.get());
        } else if (genreId.isEmpty() && year.isPresent()) {
            sqlRequest = String.format("SELECT l.film_id AS f_id, " +
                                       "       l.user_id AS u_id " +
                                       "FROM likes AS l " +
                                       "WHERE l.film_id=ANY (SELECT film_id " +
                                       "                FROM films " +
                                       "                WHERE EXTRACT(YEAR FROM releaseDate)=%s)", year.get());
        } else if (genreId.isPresent()) {
            sqlRequest = String.format("SELECT l.film_id AS f_id, " +
                                       "       l.user_id AS u_id " +
                                       "FROM likes AS l " +
                                       "WHERE l.film_id=ANY (SELECT f.film_id " +
                                       "                     FROM (SELECT film_id " +
                                       "                           FROM films " +
                                       "                           WHERE EXTRACT(YEAR FROM releaseDate)=%s) AS f " +
                                       "                     JOIN (SELECT film_id " +
                                       "                           FROM film_genre " +
                                       "                           WHERE id=%s) AS g ON f.film_id=g.film_id)",
                                       year.get(), genreId.get());
        } else {
            sqlRequest ="SELECT film_id AS f_id, " +
                        "       user_id AS u_id " +
                        "FROM likes";
        }
        return sqlRequest;
    }

    @Override
    public void deleteAllLikesFilm(long idFilm) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=?", idFilm);
    }
}
