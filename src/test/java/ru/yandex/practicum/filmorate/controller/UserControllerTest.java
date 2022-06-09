
package ru.yandex.practicum.filmorate.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc

public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(1L, "qwerty@mail.ru", "test", "Luce", LocalDate.of(2000, 1, 1));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @MethodSource("invalidUserSource")
    @ParameterizedTest(name = "{0}")
    void createInvalidUserTest(String s, User user) throws Exception {
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    private static Stream<Arguments> invalidUserSource() {
        return Stream.of(
                Arguments.of("Invalid birthday",
                        new User(1L,
                                "qwerty@mail.ru",
                                "test",
                                "Lucy",
                                LocalDate.of(2100, 1, 1))),
                Arguments.of("Invalid email",
                        new User(1L,
                                "qwertymail.ru",
                                "test",
                                "Lucy",
                                LocalDate.of(2000, 1, 1))),
                Arguments.of("Invalid id",
                        new User(-1L,
                                "qwerty@mail.ru",
                                "test",
                                "Lucy",
                                LocalDate.of(2000, 1, 1))),
                Arguments.of("Invalid login",
                        new User(1L,
                                "qwerty@mail.ru",
                                "te st",
                                "Lucy",
                                LocalDate.of(2000, 1, 1))),
                Arguments.of("Invalid user = null", null)
        );
   }
}

