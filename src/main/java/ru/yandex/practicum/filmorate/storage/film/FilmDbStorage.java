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

import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        film.setId(num.longValue());
        return film;
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
        Film film1 = getFilmById(film.getId()).get();
        return film1;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.film_id AS film_id, " +
                "                                              f.name , " +
                "                                              f.releaseDate AS rel_date, " +
                "                                              f.description AS description, " +
                "                                              f.duration AS duration, " +
                "                                              f.rate AS rate, " +
                "                                              m.id AS m_id, " +
                "                                              m.name  AS  m_name  FROM  films  AS  f  " +
                " LEFT JOIN mpa AS m ON f.mpa_id=m.id WHERE f.film_id=?", filmId);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong(1),
                    filmRows.getString(2),
                    filmRows.getDate(3).toLocalDate(),
                    filmRows.getString(4),
                    filmRows.getInt(5),
                    filmRows.getInt(6),
                    new Mpa(filmRows.getInt(7), filmRows.getString(8)),
                    null,
                    null,
                    null
            );
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод возвращает упорядоченный по убыванию количества лайков список из limit фильмов отфильтрованный по жанру и
     * году. Если в БД нет ни одного фильма, удовлетворяющего заданным условиям, то метод вернет пустой список.
     *
     * @param genreId идентификатор жанра фильма.
     * @param year    год выпуска фильма;
     * @param limit   максимальное количество фильмов, которое вернет метод;
     * @return отфильтрованный список фильмов, упорядоченный по убыванию количества лайков; пустой список, если в БД
     *         нет ни одного фильма
     */
    @Override
    public List<Film> getFilteredListOfFilms(Optional<Integer> genreId, Optional<Integer> year, Optional<Integer> limit) {
        return jdbcTemplate.queryForStream(getSQLRequestByParameters(genreId, year, limit),
                (rs, num) -> new Film(rs.getLong("f_id"), rs.getString("name"), rs.getDate("rel_date").toLocalDate(),
                             rs.getString("description"), rs.getInt("duration"), rs.getInt("rate"),
                             new Mpa(rs.getInt("m_id"), rs.getString("m_name")), null, null, null))
                .collect(Collectors.toList());
    }

    /**
     * Метод возвращает строку SQL запроса с учетом значения параметров genreId, year, limit.
     *
     * @param genreId   идентификатор жанра;
     * @param year  год выпуска фильма;
     * @param limit максимальное количество фильмов, которое нужно получить;
     * @return  строка с SQL запросом.
     */
    private String getSQLRequestByParameters(Optional<Integer> genreId, Optional<Integer> year, Optional<Integer> limit) {
        String sqlRequest;

        if (genreId.isPresent() && year.isPresent()) {
            sqlRequest = String.format("SELECT f.film_id AS f_id, " +
                                       "       f.name AS name, " +
                                       "       f.releaseDate AS rel_date, " +
                                       "       f.description AS description, " +
                                       "       f.duration AS duration, " +
                                       "       l.rate AS rate, " +
                                       "       m.id AS m_id, " +
                                       "       m.name AS m_name " +
                                       "FROM (SELECT * " +
                                       "      FROM film_genre " +
                                       "      WHERE id=%s) AS fg " +
                                       "INNER JOIN (SELECT * " +
                                       "            FROM films " +
                                       "            WHERE EXTRACT(YEAR FROM releaseDate)=%s) AS f " +
                                       "ON fg.film_id=f.film_id " +
                                       "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                                       "LEFT JOIN (SELECT film_id, COUNT(user_id) AS rate " +
                                       "           FROM likes " +
                                       "           GROUP BY film_id) AS l ON f.film_id=l.film_id " +
                                       "ORDER BY rate DESC ", genreId.get(), year.get());
        } else if (genreId.isPresent()) {
            sqlRequest = String.format("SELECT f.film_id AS f_id, " +
                                       "       f.name AS name, " +
                                       "       f.releaseDate AS rel_date, " +
                                       "       f.description AS description, " +
                                       "       f.duration AS duration, " +
                                       "       l.rate AS rate, " +
                                       "       m.id AS m_id, " +
                                       "       m.name AS m_name " +
                                       "FROM (SELECT film_id " +
                                       "      FROM film_genre " +
                                       "      WHERE id=%s) AS fg " +
                                       "LEFT JOIN films AS f ON fg.film_id=f.film_id " +
                                       "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                                       "LEFT JOIN (SELECT film_id, COUNT(user_id) AS rate " +
                                       "           FROM likes " +
                                       "           GROUP BY film_id) AS l ON f.film_id=l.film_id " +
                                       "ORDER BY rate DESC", genreId.get());
        } else if (year.isPresent()) {
            sqlRequest = String.format("SELECT f.film_id AS f_id, " +
                                       "       f.name AS name, " +
                                       "       f.releaseDate AS rel_date, " +
                                       "       f.description AS description, " +
                                       "       f.duration AS duration, " +
                                       "       l.rate AS rate, " +
                                       "       m.id AS m_id, " +
                                       "       m.name AS m_name " +
                                       "FROM (SELECT * " +
                                       "      FROM films " +
                                       "      WHERE EXTRACT(YEAR FROM releaseDate)=%s) AS f " +
                                       "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                                       "LEFT JOIN (SELECT film_id, COUNT(user_id) AS rate " +
                                       "           FROM likes " +
                                       "           GROUP BY film_id) AS l ON f.film_id=l.film_id " +
                                       "ORDER BY rate DESC", year.get());
        } else {
            sqlRequest = "SELECT f.film_id AS f_id, " +
                         "       f.name AS name, " +
                         "       f.releaseDate AS rel_date, " +
                         "       f.description AS description, " +
                         "       f.duration AS duration, " +
                         "       l.rate AS rate, " +
                         "       m.id AS m_id, " +
                         "       m.name AS m_name " +
                         "FROM films AS f " +
                         "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                         "LEFT JOIN (SELECT film_id, COUNT(user_id) AS rate " +
                         "           FROM likes " +
                         "           GROUP BY film_id) AS l ON f.film_id=l.film_id " +
                         "ORDER BY rate DESC";
        }
        return limit.map(integer -> sqlRequest + String.format(" LIMIT %s", integer)).orElse(sqlRequest);
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

    @Override
    public Collection<Film> getCommonFilms(long userId, long friendId) {
        List<Film> commonFilms = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT film_id FROM likes WHERE user_id = ? INTERSECT " +
                "SELECT film_id FROM likes WHERE user_id = ?", userId, friendId);
        while (rowSet.next()) {
                commonFilms.add(getFilmById(rowSet.getInt("film_id")).get());
        }
        return commonFilms;
    }

    @Override
    public void deleteFilm(long id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public List<Film> search(String query, List<String> by) {
        List<Film> filmList = new ArrayList<>();
        String stringSearch = "%" + query.toLowerCase() + "%";
        if (by.contains("title")) {
            filmList.addAll(jdbcTemplate.query("SELECT film_id FROM films WHERE LOWER(name) LIKE ?",
                    ((rs, rowNum) -> getFilmById(rs.getLong("film_id")).get()), stringSearch));
        }
        if (by.contains("director")) {
            filmList.addAll(jdbcTemplate.query("SELECT f.film_id FROM films AS f " +
                            "JOIN film_director AS fd ON f.film_id=fd.film_id " +
                            "JOIN directors AS d ON fd.id=d.id WHERE LOWER(d.name) LIKE ?",
                    ((rs, rowNum) -> getFilmById(rs.getLong("film_id")).get()), stringSearch));
        }
        return filmList;
    }
}
