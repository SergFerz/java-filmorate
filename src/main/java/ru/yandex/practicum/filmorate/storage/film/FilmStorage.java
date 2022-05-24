package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> getFilmById(long id);

    long getNextId();
}
