package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Data
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

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
        List<Film> filmList = new ArrayList<>(films.values());
        return filmList;
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм");
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " обновлен.");
        return film;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }
}
