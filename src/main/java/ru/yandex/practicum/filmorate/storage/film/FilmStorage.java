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

    /**
     * Метод возвращает упорядоченный по убыванию количества лайков список из limit фильмов отфильтрованный по жанру и
     * году. Если в БД нет ни одного фильма, удовлетворяющего заданным условиям, то метод вернет пустой список.
     *
     * @param genreId   идентификатор жанра фильма.
     * @param year      год выпуска фильма;
     * @param limit     максимальное количество фильмов, которое вернет метод;
     * @return  отфильтрованный список фильмов, упорядоченный по убыванию количества лайков; пустой список, если в БД
     *          нет ни одного фильма
     */
    List<Film> getFilteredListOfFilms(Optional<Integer> genreId, Optional<Integer> year, Optional<Integer> limit);

    /**
     * Метод увеличивает на 1 поле rate таблицы films для фильма с идентификатором filmId.
     *
     * @param filmId    идентификатор фильма.
     */
    void incrementFilmRate(long filmId);

    /**
     * Метод уменьшает на 1 поле rate таблицы films для фильма с идентификатором filmId.
     *
     * @param filmId    идентификатор фильма.
     */
    void decrementFilmRate(long filmId);

    List<Film> getSortedByLikesFilmsOfDirector(long directorId);

    List<Film> getSortedByYearFilmsOfDirector(long directorId);

    Collection<Film> getCommonFilms(long userId, long friendId);

    void deleteFilm(long id);

    List<Film> search(String query, List<String> by);
}
