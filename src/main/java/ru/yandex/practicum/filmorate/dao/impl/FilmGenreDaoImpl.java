package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Метод возвращает ассоциативный массив, где ключом является идентификатор фильма, а значением список жанров,
     * связанных с этим фильмом. Данный массив будет содержать только фильмы, удовлетворяющие заданным параметрам genreId,
     * year.
     *
     * @param genreId идентификатор жанра,
     * @param year    год выпуска фильмов;
     * @return ассоциативный массив, где ключом является идентификатор фильма, а значением список жанров,
     * связанных с этим фильмом.
     */
    @Override
    public Map<Long, Set<Genre>> getGenresForFilteredFilms(Optional<Integer> genreId, Optional<Integer> year) {
        Map<Long, Set<Genre>> filmsGenres = new HashMap<>();
        jdbcTemplate.query(getSQLRequestByParameters(genreId, year), rs -> {
            Long filmId = rs.getLong("f_id");
            Set<Genre> genres = filmsGenres.getOrDefault(filmId, new HashSet<>());
            genres.add(new Genre(rs.getInt("g_id"), rs.getString("g_name")));
            filmsGenres.put(filmId, genres);
        });
        return filmsGenres;
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
            sqlRequest = String.format("SELECT fg.film_id AS f_id, " +
                                       "       g.id AS g_id, " +
                                       "       g.name AS g_name " +
                                       "FROM (SELECT * " +
                                       "      FROM film_genre " +
                                       "      WHERE film_id=ANY (SELECT film_id " +
                                       "                         FROM film_genre " +
                                       "                         WHERE id=%s)) AS fg " +
                                       "LEFT JOIN genres AS g ON fg.id=g.id", genreId.get());
        } else if (genreId.isEmpty() && year.isPresent()) {
            sqlRequest = String.format("SELECT fg.film_id AS f_id, " +
                                       "       g.id AS g_id, " +
                                       "       g.name AS g_name " +
                                       "FROM (SELECT * " +
                                       "      FROM film_genre " +
                                       "      WHERE film_id=ANY (SELECT film_id " +
                                       "                         FROM films " +
                                       "                         WHERE EXTRACT(YEAR FROM releaseDate)=%s)) AS fg " +
                                       "LEFT JOIN genres AS g ON fg.id=g.id", year.get());
        } else if (genreId.isPresent()) {
            sqlRequest = String.format("SELECT fg.film_id AS f_id, " +
                                       "       g.id AS g_id, " +
                                       "       g.name AS g_name " +
                                       "FROM (SELECT * " +
                                       "      FROM film_genre " +
                                       "      WHERE film_id=ANY (SELECT film_id " +
                                       "                         FROM film_genre " +
                                       "                         WHERE film_id=ANY (SELECT film_id " +
                                       "                         FROM films " +
                                       "                         WHERE EXTRACT(YEAR FROM releaseDate)=%s) " +
                                       "                         AND id=%s)) AS fg " +
                                       "LEFT JOIN genres AS g ON fg.id=g.id", year.get(), genreId.get());
        } else {
            sqlRequest = "SELECT fg.film_id AS f_id, " +
                         "       g.id AS g_id, " +
                         "       g.name AS g_name " +
                         "FROM film_genre AS fg " +
                         "LEFT JOIN genres AS g ON fg.id=g.id";
        }
        return sqlRequest;
    }
}
