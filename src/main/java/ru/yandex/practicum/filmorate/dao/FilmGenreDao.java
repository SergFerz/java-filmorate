package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmGenreDao {

    Map<Long, Set<Genre>> getGenresForAllFilms();

    Set<Genre> getGenresByFilmId(Long filmId);

    Set<Genre> updateGenresFilm(Film film);

    Set<Genre> createGenresFilm(Film film);
}
