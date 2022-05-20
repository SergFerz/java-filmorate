package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addLike(long idFilm, long idUser) {
        if (idFilm < 0 || idUser < 0) {
            throw new ValidationException("Введено некорректное значение id");
        }
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        film.getLikes().add(idUser);
        filmStorage.update(film);
    }

    public void deleteLike(long idFilm, long idUser) {
        if (idFilm < 0 || idUser < 0) {
            throw new ValidationException("Введено некорректное значение id");
        }
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        film.getLikes().remove(idUser);
        filmStorage.update(film);
    }

    public Integer getAmountLikes(long idFilm) {
        if (idFilm < 0) {
            throw new ValidationException("Введено некорректное значение id");
        }
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        return film.getLikes().size();
    }

    public Collection<Film> getTopFilm(int count) {
        if (count < 1) {
            throw new ValidationException("Введено некорректное значение count");
        }
        List<Film> films = (List<Film>) filmStorage.findAll();
        films = films.stream().sorted((f1, f2) -> f1.compareTo(f2))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Film getFilmById(long id) {
        if (id < 0) {
            throw new ValidationException("Введено некорректное значение id");}
        List<Film> films = (List<Film>) filmStorage.findAll();
        return films.get(Math.toIntExact(id));
    }
}
