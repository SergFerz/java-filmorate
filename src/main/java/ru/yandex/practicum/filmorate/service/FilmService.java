package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.getAllFilms();
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
        Film film = getFilmById(idFilm);
        film.getLikes().add(idUser);
        filmStorage.update(film);
        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        Film film = getFilmById(idFilm);
        Set<Long> likes = film.getLikes();
        if (!likes.contains(idUser)) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        film.deleteLike(idUser);
        filmStorage.update(film);
        return film;
    }

    public Integer getAmountLikes(long idFilm) {
        Film film = getFilmById(idFilm);
        return film.getLikes().size();
    }

    public List<Film> getTopFilm(Integer count) {
        if (count < 1) {
            throw new ValidationException("Введено некорректное значение count");
        }
        List<Film> films = (List<Film>) filmStorage.getAllFilms();
        films = films.stream().sorted((f1, f2) -> f1.compareTo(f2))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return film;
    }

    private Film validateFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Описание не должно превышать 200 символов");
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
}
