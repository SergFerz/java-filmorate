package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmGenreDao {
    /**
     * Метод возвращает ассоциативный массив, где ключом является идентификатор фильма, а значением список жанров,
     * связанных с этим фильмом. Данный массив будет содержать только фильмы, удовлетворяющие заданным параметрам genreId,
     * year.
     *
     * @param genreId   идентификатор жанра,
     * @param year      год выпуска фильмов;
     * @return ассоциативный массив, где ключом является идентификатор фильма, а значением список жанров,
     *         связанных с этим фильмом.
     */
    Map<Long, Set<Genre>> getGenresForFilteredFilms(Optional<Integer> genreId, Optional<Integer> year);

    Map<Long, Set<Genre>> getGenresForAllFilms();

    Set<Genre> getGenresByFilmId(Long filmId);

    Set<Genre> updateGenresFilm(Film film);

    Set<Genre> createGenresFilm(Film film);
}
