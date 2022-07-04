package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@Valid @PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/films/{filmId}")
    public void deleteFilmById(@Valid @PathVariable long filmId) {filmService.deleteFilmById(filmId);}

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilteredListOfFilms(@RequestParam("count") Optional<Integer> limit,
                                             @RequestParam("genreId") Optional<Integer> genreId,
                                             @RequestParam("year") Optional<Integer> year) {
        return filmService.getFilteredListOfFilms(genreId, year, limit);
    }

    public List<Film> getTopFilm(@RequestParam(defaultValue = "10", required = false, name = "count") Integer count) {
        return filmService.getTopFilm(count);
    }

    @DeleteMapping("/films/{filmId}")
    public ResponseEntity<?> deleteFilm(@PathVariable("filmId") long filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getSortedFilmsOfDirector(@PathVariable long directorId,
                                               @RequestParam String sortBy) {
        return filmService.getSortedFilmsOfDirector(directorId, sortBy);
    }

    @GetMapping("/films/common") // ?id={userId}&friendId={friendId}
    public Collection<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
