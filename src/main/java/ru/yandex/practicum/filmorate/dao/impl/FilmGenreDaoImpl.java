package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
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

    @Override
    public Map<Long, Set<Genre>> getGenresForAllFilms() {
        Map<Long, Set<Genre>> filmsGenres = new HashMap<>();
        jdbcTemplate.query("SELECT fg.film_id, g.id, g.name FROM film_genre AS fg LEFT JOIN genres AS g ON fg.id=g.id",
                rs -> {
            Long filmId = rs.getLong(1);
            Set<Genre> genres = filmsGenres.getOrDefault(filmId, new HashSet<>());
            genres.add(new Genre(rs.getInt(2), rs.getString(3)));
            filmsGenres.put(filmId, genres);
        });
        return filmsGenres;
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT g.id AS id, g.name AS name FROM film_genre AS fg " +
                "LEFT JOIN genres AS g ON fg.id=g.id WHERE fg.film_id=?", filmId);
        while (genreRows.next()) {
            genres.add(new Genre(genreRows.getInt("id"), genreRows.getString("name")));
        }
        if (genres.isEmpty()) {
            genres = null;
        }
        return genres;
    }

    @Override
    public Set<Genre> updateGenresFilm(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", film.getId());
        film.getGenres().stream()
                .forEach(genre -> jdbcTemplate.update("INSERT INTO film_genre(film_id, id) VALUES (?,?);",
                        film.getId(), genre.getId()));
        return getGenresByFilmId(film.getId());
    }

    @Override
    public Set<Genre> createGenresFilm(Film film) {
        film.getGenres().stream().
                forEach(genre -> jdbcTemplate.update("INSERT INTO film_genre(film_id, id) VALUES (?,?);",
                        film.getId(), genre.getId()));
        return getGenresByFilmId(film.getId());
    }
}
