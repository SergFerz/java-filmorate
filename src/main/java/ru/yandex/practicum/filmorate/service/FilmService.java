package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addLike(Long idFilm, Long idUser) {
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        film.getLikes().add(idUser);
        filmStorage.update(film);
    }

    public void deleteLike(Long idFilm, Long idUser) {
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        film.getLikes().remove(idUser);
        filmStorage.update(film);
    }

    public Integer getAmountLikes(Long idFilm) {
        List<Film> films = (List<Film>) filmStorage.findAll();
        Film film = films.get(Math.toIntExact(idFilm));
        return film.getLikes().size();
    }

    public Collection<Film> getTopFilm(int count) {
        List<Film> films = (List<Film>) filmStorage.findAll();
        films = films.stream().sorted((f1, f2) -> f1.compareTo(f2))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Film getFilmById(Long id) {
        List<Film> films = (List<Film>) filmStorage.findAll();
        return films.get(Math.toIntExact(id));
    }
}
