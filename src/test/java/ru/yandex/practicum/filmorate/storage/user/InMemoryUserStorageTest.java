package ru.yandex.practicum.filmorate.storage.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
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
        User user = new User(1L,
                "qwerty@mail.ru",
                "testLogin",
                "Luce",
                LocalDate.of(2000, 1, 1));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_createValidUserResponseShouldBeOk() {
        User user1 = new User(1L,
                "qrty@yandex.ru",
                "test1",
                "Mark",
                LocalDate.of(1995, 12, 10));
        userStorage.create(user1);
        User user2 = new User(2L,
                "qwerty@mail.ru",
                "testLogin",
                "Luce",
                LocalDate.of(2000, 1, 1));
        userStorage.create(user2);
        User user3 = new User(3L,
                "qrty@yandex.ru",
                "test3",
                "Liza",
                LocalDate.of(1997, 12, 10));
        userStorage.create(user3);
        User user4 = new User(4L,
                "qwedftgjjdrty@mail.ru",
                "testLogin4",
                "Alex",
                LocalDate.of(2002, 3, 17));
        userStorage.create(user4);
        userStorage.addFriend(1, 2, "UNCONFIRM");
        userStorage.addFriend(1, 3, "UNCONFIRM");
        userStorage.addFriend(1, 4, "UNCONFIRM");
        userStorage.addFriend(2, 3, "UNCONFIRM");
        userStorage.addFriend(2, 4, "UNCONFIRM");
        assertEquals(user2, userStorage.getUserById(2).get());
        assertEquals(4, userStorage.getAllUsers().size());
    }
}