package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class FilmDirectorDaoImpl implements FilmDirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public FilmDirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Director> getDirectorsByFilmId(long filmId) {
        Set<Director> directors = new HashSet<>();
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("SELECT d.id AS id, d.name AS name FROM film_director AS fd " +
                "LEFT JOIN directors AS d ON fd.id=d.id WHERE fd.film_id=?", filmId);
        while (directorRows.next()) {
            directors.add(new Director(directorRows.getInt("id"), directorRows.getString("name")));
        }
        return directors;
    }

    @Override
    public Set<Director> updateDirectorsFilm(Film film) {
        jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", film.getId());
        film.getDirectors().stream()
                .forEach(director -> jdbcTemplate.update("INSERT INTO film_director(film_id, id) VALUES (?,?)",
                        film.getId(), director.getId()));
        return getDirectorsByFilmId(film.getId());
    }

    @Override
    public Set<Director> createDirectorsFilm(Film film) {
        film.getDirectors().stream()
                .forEach(director -> jdbcTemplate.update("INSERT INTO film_director(film_id, id) VALUES (?,?)",
                        film.getId(), director.getId()));
        return getDirectorsByFilmId(film.getId());
    }

    @Override
    public Map<Long, Set<Director>> getDirectorsForAllFilms() {
        Map<Long, Set<Director>> filmDirectors = new HashMap<>();
        jdbcTemplate.query("SELECT fd.film_id, d.id, d.name FROM film_director AS fd LEFT JOIN directors AS d ON fd.id=d.id",
                rs -> {
                    Long filmId = rs.getLong(1);
                    Set<Director> directors = filmDirectors.getOrDefault(filmId, new HashSet<>());
                    directors.add(new Director(rs.getInt(2), rs.getString(3)));
                    filmDirectors.put(filmId, directors);
                });
        return filmDirectors;
    }

    @Override
    public void deleteDirectorsFilm(Film film) {
        jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", film.getId());
    }
}
