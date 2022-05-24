package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.getAllFilms();
    }

    public Film create(Film film) {return filmStorage.create(film);}

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(long idFilm, long idUser) {
        if (idFilm < 1 || idUser < 1) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        Film film = filmStorage.getFilmById(idFilm);
        film.getLikes().add(idUser);
        filmStorage.update(film);
        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        if (idFilm < 1 || idUser < 1) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        Film film = filmStorage.getFilmById(idFilm);
        Set<Long> likes = film.getLikes();
        if (!likes.contains(idUser)) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        film.deleteLike(idUser);
        filmStorage.update(film);
        return film;
    }

    public Integer getAmountLikes(long idFilm) {
        if (idFilm < 1) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        List<Film> films = (List<Film>) filmStorage.getAllFilms();
        Film film = films.get(Math.toIntExact(idFilm));
        return film.getLikes().size();
    }

    public List<Film> getTopFilm(Integer count) {
        if (count < 1) {
            throw new NotFoundException("Введено некорректное значение count");
        }
        List<Film> films = (List<Film>) filmStorage.getAllFilms();
        films = films.stream().sorted((f1, f2) -> f1.compareTo(f2))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Film getFilmById(long id) {return filmStorage.getFilmById(id);}
}
