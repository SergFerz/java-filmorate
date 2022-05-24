package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Long, Film> films = new HashMap<>();
    private long counterId = 1L;

    public long getNextId() {
        while (films.containsKey(counterId)) {
            counterId++;
        }
        return counterId;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        List<Film> filmList = new ArrayList<>();
        if (!films.isEmpty()) {
            for (Film f : films.values()) {
                filmList.add(f);
            }
        }
        return filmList;
    }

    @Override
    public Film create(Film film) {
        validatefilm(film);
        log.debug("Добавлен новый фильм");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validatefilm(film);
        /*if (!films.containsValue(film)) {
            throw new NotFoundException("Этот film не содержится в реестре");
        }*/
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " обновлен.");
        return film;
    }

    private Film validatefilm(Film film) {
        if (film.getId() < 0) {
            log.debug("Введено некорректное значение id");
            throw new NotFoundException("Введено некорректное значение id");
        } else if (film.getName().isBlank()){
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getDescription().length() > 200) {
            log.debug("Описание не должно превышать 200 символов");
            throw new ValidationException("Описание не должно превышать 200 символов");
        }  else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.debug("Введен некорректный день релиза");
            throw new ValidationException("Введен некорректный день релиза");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Введено некорректное значение duration");
        } else if (film.getId() == 0) {film.setId(getNextId());}
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        if (id < 1) {
            throw new NotFoundException("Введено некорректное значение id");}
        return films.get(id);
    }
}
