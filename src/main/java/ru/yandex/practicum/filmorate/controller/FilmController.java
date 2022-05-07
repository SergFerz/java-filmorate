package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public void create(@RequestBody Film film) {
        if (film.getName().isBlank()) {
            log.debug("Введено некорректное название фильма");
            throw new ValidationException("Введено некорректное название фильма");
        } else if (film.getDescription().length() > 200) {
            log.debug("Описание не должно превышать 200 символов");
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getDuration() < 0) {
            log.debug("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.debug("Введен некорректный день рождения");
            throw new ValidationException("Введен некорректный день рождения");
        } else if (film.getId() < 0) {
            log.debug("Некорректный идентификатор id");
            throw new ValidationException("Некорректный идентификатор id");
        } else {
            log.debug("Добавлен новый фильм");
            films.put(film.getId(), film);
        }
    }

    @PutMapping("/films")
    public void put(@RequestBody Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " обновлен.");
    }
}
