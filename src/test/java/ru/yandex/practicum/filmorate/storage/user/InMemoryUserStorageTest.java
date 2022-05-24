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

    @Autowired
    UserController userController;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(1L, "qwerty@mail.ru", "testLogin", "Luce", LocalDate.of(2000, 1, 1));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_createValidUserResponseShouldBeOk()  {
        User user1 = new User(1L, "qrty@yandex.ru", "test1", "Mark", LocalDate.of(1995, 12, 10));
        userController.create(user1);
        User user2 = new User(2L, "qwerty@mail.ru", "testLogin", "Luce", LocalDate.of(2000, 1, 1));
        userController.create(user2);
        User user3 = new User(3L, "qrty@yandex.ru", "test3", "Liza", LocalDate.of(1997, 12, 10));
        userController.create(user3);
        User user4 = new User(4L, "qwedftgjjdrty@mail.ru", "testLogin4", "Alex", LocalDate.of(2002, 3, 17));
        userController.create(user4);

        userController.addFriend(1, 2);
        userController.addFriend(1, 3);
        userController.addFriend(1, 4);
        userController.addFriend(2, 3);
        userController.addFriend(2, 4);
        System.out.println(userController.getAllFriends(2));
        //System.out.println(user2.getFriends());
        //System.out.println(userController.getCommonFriends(1L, 2L));

        assertEquals(2,userController.getCommonFriends(1L, 2L).size());
        assertEquals(user2,userController.findUserById(2L));
        assertEquals(4,userController.getAllUsers().size());
    }


}