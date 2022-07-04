package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplementedMethod;
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

    /**
     * Метод возвращает упорядоченный по убыванию количества лайков список из limit фильмов отфильтрованный по жанру и
     * году. Если в БД нет ни одного фильма, удовлетворяющего заданным условиям, то метод вернет пустой список.
     *
     * @param genreId идентификатор жанра фильма.
     * @param year    год выпуска фильма;
     * @param limit   максимальное количество фильмов, которое вернет метод;
     * @return отфильтрованный список фильмов, упорядоченный по убыванию количества лайков; пустой список, если в БД
     * нет ни одного фильма
     */
    @Override
    public List<Film> getFilteredListOfFilms(Optional<Integer> genreId, Optional<Integer> year, Optional<Integer> limit) {
        throw new NotImplementedMethod("Метод InMemoryFilmStorage.getFilteredListOfFilms() - не реализован.");
    }

    /**
     * Метод увеличивает на 1 поле rate таблицы films для фильма с идентификатором filmId.
     *
     * @param filmId идентификатор фильма.
     */
    @Override
    public void incrementFilmRate(long filmId) {
        throw new NotImplementedMethod("Метод InMemoryFilmStorage.incrementFilmRate() - не реализован.");
    }

    /**
     * Метод уменьшает на 1 поле rate таблицы films для фильма с идентификатором filmId.
     *
     * @param filmId идентификатор фильма.
     */
    @Override
    public void decrementFilmRate(long filmId) {
        throw new NotImplementedMethod("Метод InMemoryFilmStorage.decrementFilmRate() - не реализован.");
   }

    @Override
    public List<Film> getSortedByLikesFilmsOfDirector(long directorId) {
        return null;
    }

    @Override
    public List<Film> getSortedByYearFilmsOfDirector(long directorId) {
        return null;
    }

    @Override
    public Collection<Film> getCommonFilms(long userId, long friendId) {
        return null;
    }
}
