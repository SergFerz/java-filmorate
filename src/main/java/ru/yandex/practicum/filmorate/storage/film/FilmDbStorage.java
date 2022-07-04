package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDao genreDao, DirectorDao directorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.directorDao = directorDao;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmIdRows = jdbcTemplate.queryForRowSet("SELECT film_id FROM films");
        while (filmIdRows.next()) {
            if (getFilmById(filmIdRows.getInt("film_id")).isPresent()) {
                films.add(getFilmById(filmIdRows.getInt("film_id")).get());
            }
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("films").usingGeneratedKeyColumns("film_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("rate", film.getRate())
                .addValue("mpa_id", film.getMpa().getId());
        Number num = jdbcInsert.executeAndReturnKey(parameters);
        if (film.getGenres() != null) {
            film.getGenres().stream().
                    forEach(genre -> jdbcTemplate.update("INSERT INTO film_genre(film_id, id) VALUES (?,?);",
                            num.longValue(), genre.getId()));
        }
        if (film.getDirectors() != null) {
            film.getDirectors().stream()
                    .forEach(director -> jdbcTemplate.update("INSERT INTO film_director(film_id, id) VALUES (?,?)",
                            num.longValue(), director.getId()));
        }
        if (getFilmById(num.longValue()).isPresent()) {
            return getFilmById(num.longValue()).get();
        } else return null;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET name=?, releaseDate=?, description=?, duration=?, rate=?, mpa_id=? WHERE film_id=?",
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        if (film.getDirectors() != null) {
            jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", film.getId());
            film.getDirectors().stream()
                    .forEach(director -> jdbcTemplate.update("INSERT INTO film_director(film_id, id) VALUES (?,?)",
                            film.getId(), director.getId()));
        } else {
            jdbcTemplate.update("DELETE FROM film_director WHERE film_id=?", film.getId());
        }

        if (film.getGenres() != null) {
            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", film.getId());
            film.getGenres().stream()
                    .forEach(genre -> jdbcTemplate.update("INSERT INTO film_genre(film_id, id) VALUES (?,?);",
                            film.getId(), genre.getId()));
        }
        Film film1 = getFilmById(film.getId()).get();

        if (film.getGenres() != null && (film1.getGenres() == null)) {
            film1.setGenres(Collections.emptySet());
        }
        if (film.getDirectors() == null) {
            film1.setDirectors(null);
        }
        return film1;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE film_id=?", id);
        while (genreRows.next()) {
            genres.add(genreDao.getGenreById(genreRows.getInt("id")).get());
        }
        if (genres.isEmpty()) {
            genres = null;
        }

        Set<Director> directors = new HashSet<>();
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_director WHERE film_id=?", id);
        while (directorRows.next()) {
            directors.add(directorDao.getDirectorById(directorRows.getLong("id")).get());
        }

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", id);
        if (filmRows.next()) {
            SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?",
                    filmRows.getInt("mpa_id"));
            mpaRow.next();
            int mpaId = mpaRow.getInt("id");
            String mpaName = mpaRow.getString("name");

            SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE film_id=?",
                    filmRows.getLong("film_id"));
            Set<Long> likes = new HashSet<>();
            if (likeRows.next()) {
                likes.add(likeRows.getLong("user_id"));
            }
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getString("description"),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    new Mpa(mpaId, mpaName),
                    genres,
                    directors,
                    likes
            );
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public List<Film> getSortedByYearFilmsOfDirector(long directorId) {
        List<Film> filmList = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films " +
                        "LEFT JOIN film_director AS fd ON films.film_id=fd.film_id WHERE fd.id=?" +
                        "ORDER BY films.releaseDate",
                directorId);
        while (filmRows.next()) {
            filmList.add(getFilmById(filmRows.getLong("film_id")).get());
        }
        return filmList;
    }

    public List<Film> getSortedByLikesFilmsOfDirector(long directorId) {
        List<Film> filmList = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films AS f " +
                "LEFT JOIN likes AS l on f.film_id=l.film_id " +
                "LEFT JOIN film_director AS fd ON f.film_id=fd.film_id WHERE fd.id=? " +
                "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC", directorId);
        while (filmRows.next()) {
            filmList.add(getFilmById(filmRows.getLong("film_id")).get());
        }
        return filmList;
    }
}
