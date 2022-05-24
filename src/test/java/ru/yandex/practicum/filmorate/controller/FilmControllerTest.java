package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FilmService filmService;

    @Autowired
    FilmController filmController;

    @Test
    void test1_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(1L, "Seven", LocalDate.of(2000, 1, 1),"test",  125, 4);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(1L, "Seven", LocalDate.of(2000, 1, 1),"test",  125, 4);
        filmController.create(film);
        //filmController.deleteLike(1, -2);

    }

    @Test
    void test3_getPopularFilm() {
        Film film1 = new Film(1L, "Seven", LocalDate.of(2000, 1, 1),"test",  125, 0);
        filmController.create(film1);
        Film film2 = new Film(2L, "Batman", LocalDate.of(2022, 1, 1),"test",  176, 4);
        filmController.create(film2);
        filmController.addLike(2, 1);
        System.out.println(filmController.getTopFilm(1));

    }


    @MethodSource("invalidFilmSource")
    @ParameterizedTest(name = "{0}")
    void createInvalidUserTest(String s, Film film) throws Exception {
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    /*private static Stream<Arguments> invalidFilmSource() {
        return Stream.of(
                Arguments.of("Invalid releaseDate",
                        new Film(1L,
                                "Seven",
                                "test",
                                LocalDate.of(1895, 12, 27),
                                125)),
                Arguments.of("Invalid name",
                        new Film(1L,
                                "",
                                "test",
                                LocalDate.of(2000, 1, 1),
                                125)),
                Arguments.of("Invalid id",
                        new Film(-1L,
                                "Seven",
                                "test",
                                LocalDate.of(2000, 1, 1),
                                125)),
                Arguments.of("Invalid description",
                        new Film(1L,
                                "Seven",
                                "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop" +
                                        "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop" +
                                        "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop" +
                                        "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop1",
                                LocalDate.of(2000, 1, 1),
                                125)),
                Arguments.of("Invalid name",
                        new Film(1L,
                                "Seven",
                                "test",
                                LocalDate.of(2000, 1, 1),
                                -125))
                //Arguments.of("Invalid film = null", null)
        );
    }*/
}