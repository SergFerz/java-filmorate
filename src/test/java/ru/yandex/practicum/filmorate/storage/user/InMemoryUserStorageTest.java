package ru.yandex.practicum.filmorate.storage.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class InMemoryUserStorageTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    InMemoryUserStorage userStorage;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(1L, "qwerty@mail.ru", "testLogin", "Luce", LocalDate.of(2000, 1, 1));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_createValidUserResponseShouldBeOk() throws JsonProcessingException {
        User user = new User(2L, "qwerty@mail.ru", "testLogin", "Luce", LocalDate.of(2000, 1, 1));
        userStorage.create(user);
        assertEquals(user,userStorage.getById(2L));
    }
}