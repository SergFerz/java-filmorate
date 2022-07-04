package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> getFilmById(long id);

    List<Film> getSortedByLikesFilmsOfDirector(long directorId);

    List<Film> getSortedByYearFilmsOfDirector(long directorId);
}
