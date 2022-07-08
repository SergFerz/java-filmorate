package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
    private final UserStorage userStorage;
    private final FilmGenreDao filmGenreStorage;
    private final DirectorService directorService;
    private final FilmDirectorDao filmDirectorDao;

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        Map<Long, Set<Genre>> genres = filmGenreStorage.getGenresForAllFilms();
        Map<Long, Set<Long>> likes = likeDao.getLikesForAllFilms();
        Map<Long, Set<Director>> directors = filmDirectorDao.getDirectorsForAllFilms();
        for (Film f : filmStorage.getAllFilms()) {
            f.setGenres(genres.get(f.getId()));
            f.setLikes(likes.get(f.getId()));
            if (directors.get(f.getId()) == null ) {
                f.setDirectors(Collections.emptySet());
            } else {f.setDirectors(directors.get(f.getId()));}
            films.add(f);
        }
        films.sort(Comparator.comparing(Film::getId));
        return films;
    }

    public Film create(Film film) {
        validateFilm(film);
        film = filmStorage.create(film);
        if (film.getDirectors() != null) {
            filmDirectorDao.createDirectorsFilm(film);
        }
        if (film.getGenres() != null) {
            filmGenreStorage.createGenresFilm(film);
        }
        return film;
    }

    public Film update(Film film) {
        Set<Genre> genres = null;
        Set<Director> directors = null;
        getFilmById(film.getId());
        validateFilm(film);
        if (film.getDirectors() != null) {
            directors = filmDirectorDao.updateDirectorsFilm(film);
        } else {filmDirectorDao.deleteDirectorsFilm(film);}
        if (film.getGenres() != null) {
            genres = filmGenreStorage.updateGenresFilm(film);
            if (film.getGenres().isEmpty()) {genres = Collections.emptySet();}
        }
        film = filmStorage.update(film);
        film.setGenres(genres);
        film.setDirectors(directors);
        return film;
    }

    public Film addLike(long idFilm, long idUser) {
        getFilmById(idFilm);
        if (userStorage.getUserById(idUser).isEmpty()) {
            throw new NotFoundException("Введено некорректное значение user_id");
        }
        likeDao.addLike(idFilm, idUser);
        Film film = getFilmById(idFilm);
        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        getFilmById(idFilm);
        if (userStorage.getUserById(idUser).isEmpty()) {
            throw new NotFoundException("Введено некорректное значение user_id");
        }
        likeDao.deleteLike(idFilm, idUser);
        return getFilmById(idFilm);
    }

    public Film getFilmById(long id) {
        Optional<Film> optionalFilm = filmStorage.getFilmById(id);
        if (optionalFilm.isEmpty()) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Введено некорректное значение film_id");
        }
        Film film = optionalFilm.get();
        film.setDirectors(filmDirectorDao.getDirectorsByFilmId(id));
        film.setLikes(likeDao.getLikesByFilmId(id));
        film.setGenres(filmGenreStorage.getGenresByFilmId(id));
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
        List<Film> filmList = new ArrayList<>();
        if (sortBy.equals("year")) {
            for (Film f : filmStorage.getSortedByYearFilmsOfDirector(directorId)) {
                filmList.add(getFilmById(f.getId()));
            }
            return filmList;
        } else if (sortBy.equals("likes")) {
            for (Film f : filmStorage.getSortedByLikesFilmsOfDirector(directorId)) {
                filmList.add(getFilmById(f.getId()));
            }
            return filmList;
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
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Введено некорректное значение user_id");
        }
        if (userStorage.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Введено некорректное значение friend_id");
        }
        return new ArrayList<>(filmStorage.getCommonFilms(userId, friendId));
    }

    public void deleteFilm(long filmId) {
        getFilmById(filmId);
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> search(String query, List<String> by) {
        List<Film> filmList = new ArrayList<>();
        for (Film f : filmStorage.search(query, by)) {
            filmList.add(getFilmById(f.getId()));
        }
        filmList.sort(Film::compareTo);
        return  filmList;
    }
}

