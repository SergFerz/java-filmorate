package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (film == null) {
            log.debug("Введено некорректное значение: null");
            throw new ValidationException("Введено некорректное значение: null");
        }else if (film.getName().isBlank()) {
            log.debug("Введено некорректное название фильма");
            throw new ValidationException("Введено некорректное название фильма");
        } else if (film.getDescription().isBlank() || film.getDescription().length() > 200) {
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
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null) {
            log.debug("Введено некорректное значение: null");
            throw new ValidationException("Введено некорректное значение: null");
        }
        if (!films.containsValue(film)) {
            throw new NullPointerException("Этот film не содержится в реестре");
        }
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " обновлен.");
        return film;
    }
}
