package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeDao likeDao;
    private final UserService userService;
    private final FilmGenreDao filmGenreStorage;
    private final DirectorService directorService;


    private final UserStorage userStorage;

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        films.sort(Comparator.comparing(Film::getId));
        return films;
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film addLike(long idFilm, long idUser) {
        getFilmById(idFilm);
        userService.getUserById(idUser);
        likeDao.addLike(idFilm, idUser);
        filmStorage.incrementFilmRate(idFilm);
        Film film = getFilmById(idFilm);
        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        userService.getUserById(idUser);
        getFilmById(idFilm);
        if (likeDao.deleteLike(idFilm, idUser) != 0) {
            filmStorage.decrementFilmRate(idFilm);
        }
        return getFilmById(idFilm);
    }

    public List<Film> getTopFilm(Integer count) {
        if (count < 1) {
            throw new ValidationException("Введено некорректное значение count");
        }
        List<Film> films = getAllFilms();
        films.sort(Film::compareTo);
        return films.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return film;
    }


    public List<Film> getFilteredListOfFilms(Optional<Integer> genreId, Optional<Integer> year, Optional<Integer> limit) {
        Map<Long, Set<Long>> likes = likeDao.getLikesForFilteredFilms(genreId, year);
        Map<Long, Set<Genre>> genres = filmGenreStorage.getGenresForFilteredFilms(genreId, year);
        return filmStorage.getFilteredListOfFilms(genreId, year, limit).stream().peek(film -> {
            film.setLikes(likes.get(film.getId()));
            film.setGenres(genres.get(film.getId()));
        }).collect(Collectors.toList());
}

    public List<Film> getSortedFilmsOfDirector(long directorId, String sortBy) {
        directorService.getDirectorById(directorId);
        if (sortBy.equals("year")) {
            return filmStorage.getSortedByYearFilmsOfDirector(directorId);
        } else if (sortBy.equals("likes")) {
            return filmStorage.getSortedByLikesFilmsOfDirector(directorId);
        } else return null;
    }

    private Film validateFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Описание не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.debug("Описание не должно превышать 200 символов");
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Введен некорректный день релиза");
            throw new ValidationException("Введен некорректный день релиза");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Введено некорректное значение duration");
        }
        return film;
    }

    public Collection<Film> getCommonFilms(long userId, long friendId) {
        userService.getUserById(userId);
        userService.getUserById(friendId);
        return new ArrayList<>(filmStorage.getCommonFilms(userId, friendId));
        }
    }