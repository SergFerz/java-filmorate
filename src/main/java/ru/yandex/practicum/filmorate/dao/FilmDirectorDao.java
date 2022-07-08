package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Set;

public interface FilmDirectorDao {

    Set<Director> getDirectorsByFilmId(long filmId);
    Set<Director> updateDirectorsFilm(Film film);

    Set<Director> createDirectorsFilm(Film film);

    Map<Long, Set<Director>> getDirectorsForAllFilms();

    void deleteDirectorsFilm(Film film);
}
