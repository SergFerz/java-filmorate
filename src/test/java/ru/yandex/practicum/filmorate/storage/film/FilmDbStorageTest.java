package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {

    @Autowired
    private FilmStorage filmStorage;

    @Test
    void getAllFilmsTest() {
        Collection<Film> films = filmStorage.getAllFilms();
        assertTrue(films.size() == 5);
    }

    @Test
    void createFilmTest() {
        Film film = new Film(0,
                "nameTestCreate",
                LocalDate.of(2010, 06, 06),
                "descriptionTest",
                120,
                0,
                new Mpa(1, "G"),
                new HashSet<>(),
                new HashSet<>()
                );
        Film filmCreated = filmStorage.create(film);
        assertTrue(film.getId() != filmCreated.getId());
        assertEquals(film.getName(), filmCreated.getName());
        assertEquals(film.getReleaseDate(), filmCreated.getReleaseDate());
        assertEquals(film.getDescription(), filmCreated.getDescription());
        assertEquals(film.getDuration(), filmCreated.getDuration());
    }

    @Test
    void updateFilmTest() {
        Film film = filmStorage.create(new Film(0,
                "nameTestUpdate",
                LocalDate.of(2000, 06, 06),
                "description Update Test",
                100,
                0,
                new Mpa(2, "PG"),
                new HashSet<>(),
                new HashSet<>()
        ));
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));
        Film filmUpdated = filmStorage.update(new Film(film.getId(),
                "name Updated",
                LocalDate.of(1999, 12, 12),
                "description Updated",
                99,
                1,
                new Mpa(3, "PG-13"),
                genres,
                new HashSet<>()
                ));
        assertEquals(film.getId(), filmUpdated.getId());
        assertNotEquals(film.getName(), filmUpdated.getName());
        assertNotEquals(film.getReleaseDate(), filmUpdated.getReleaseDate());
        assertNotEquals(film.getDescription(), filmUpdated.getDescription());
        assertNotEquals(film.getDuration(), filmUpdated.getDuration());
        assertNotEquals(film.getRate(), filmUpdated.getRate());
        assertNotEquals(film.getMpa().getId(), filmUpdated.getMpa().getId());
        assertNotEquals(film.getGenres(), filmUpdated.getGenres());
    }

    @Test
    void getFilmByIdTest() {
        Set<Genre> genres = new HashSet() {{
            add(new Genre(1, "Комедия"));
            add(new Genre(2, "Драма"));
        }};
        Set<Long> likes = new HashSet(){{
            add(1);
            add(2);
            add(3);
        }};
        Film film = filmStorage.create(new Film(0,
                "name FilmById",
                LocalDate.of(2020, 10, 10),
                "description FilmById",
                80,
                0,
                new Mpa(2, "PG"),
                genres,
                likes
        ));
        Film filmById = filmStorage.getFilmById(film.getId()).get();
        assertEquals(film.getName(), filmById.getName());
        assertEquals(film.getReleaseDate(), filmById.getReleaseDate());
        assertEquals(film.getDescription(), filmById.getDescription());
        assertEquals(film.getDuration(), filmById.getDuration());
        assertEquals(film.getRate(), filmById.getRate());
        assertEquals(film.getMpa().getId(), filmById.getMpa().getId());
        assertEquals(film.getGenres(), filmById.getGenres());
        assertEquals(film.getLikes(), filmById.getLikes());
    }
}