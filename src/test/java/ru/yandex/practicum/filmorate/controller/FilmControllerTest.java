package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    @Autowired
    private UserService userService;

    @Test
    void getAllFilms() {
        Collection<Film> films = filmController.getAllFilms();
        assertTrue(films.size() == 5);
    }

    @Test
    void createTest() {
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
        Film filmCreated = filmController.create(film);
        assertTrue(film.getId() != filmCreated.getId());
        assertEquals(film.getName(), filmCreated.getName());
        assertEquals(film.getReleaseDate(), filmCreated.getReleaseDate());
        assertEquals(film.getDescription(), filmCreated.getDescription());
        assertEquals(film.getDuration(), filmCreated.getDuration());
    }

    @Test
    void updateTest() {
        Film film = filmController.create(new Film(0,
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
        Film filmUpdated = filmController.update(new Film(film.getId(),
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
    void findFilmByIdTest() {
        Set<Genre> genres = new HashSet() {{
            add(new Genre(1, "Комедия"));
            add(new Genre(2, "Драма"));
        }};
        Set<Long> likes = new HashSet(){{
            add(1);
            add(2);
            add(3);
        }};
        Film film = filmController.create(new Film(0,
                "name FilmById",
                LocalDate.of(2020, 10, 10),
                "description FilmById",
                80,
                0,
                new Mpa(2, "PG"),
                genres,
                likes
        ));
        Film filmById = filmController.findFilmById(film.getId());
        assertEquals(film.getName(), filmById.getName());
        assertEquals(film.getReleaseDate(), filmById.getReleaseDate());
        assertEquals(film.getDescription(), filmById.getDescription());
        assertEquals(film.getDuration(), filmById.getDuration());
        assertEquals(film.getRate(), filmById.getRate());
        assertEquals(film.getMpa().getId(), filmById.getMpa().getId());
        assertEquals(film.getGenres(), filmById.getGenres());
        assertEquals(film.getLikes(), filmById.getLikes());
    }

    @Test
    void addLike() {
        Film film = filmController.create(new Film(0,
                "nameTestAddLike",
                LocalDate.of(2000, 06, 06),
                "description Add Like Test",
                100,
                0,
                new Mpa(2, "PG"),
                new HashSet<>(),
                new HashSet<>()
        ));
        User user = userService.create(new User(0,
                "user1_test@gmail.com",
                "user1",
                "name1",
                LocalDate.of(2001, 01,01)));
        assertEquals(0, film.getLikes().size());
        film = filmController.addLike(film.getId(), user.getId());
        assertEquals(1, film.getLikes().size());
    }

    @Test
    void deleteLikeTest() {
        Film film = filmController.create(new Film(0,
                "nameTestDeleteLike",
                LocalDate.of(2000, 06, 06),
                "description Delete Like Test",
                100,
                0,
                new Mpa(2, "PG"),
                new HashSet<>(),
                new HashSet<>()
        ));
        User user = userService.create(new User(0,
                "user2_test@gmail.com",
                "user2",
                "name2",
                LocalDate.of(2002, 02,02)));
        assertEquals(0, film.getLikes().size());
        film = filmController.addLike(film.getId(), user.getId());
        assertEquals(1, film.getLikes().size());
        film = filmController.deleteLike(film.getId(), user.getId());
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void getTopFilmTest() {
        List<Film> films = filmController.getTopFilm(10);
        assertEquals(6, films.size());
        assertEquals(5, filmController.getTopFilm(5).size());
        assertEquals(1, filmController.getTopFilm(1).size());
    }

    @Test
    void getTopFilmTestInvalidCount() {
        assertThrows(ValidationException.class,
                ()->{filmController.getTopFilm(-1);});
    }
}