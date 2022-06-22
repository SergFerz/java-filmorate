package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void getAllUsersTest() {
        List<User> users = userController.getAllUsers();
        assertEquals(4, users.size());
    }

    @Test
    void createTest() {
        User user = new User(0,
                "user1_test@gmail.com",
                "user1",
                "name1",
                LocalDate.of(2001, 01,01));
        User userCreated = userController.create(user);
        assertNotEquals(user.getId(), userCreated.getId());
        assertEquals(user.getEmail(), userCreated.getEmail());
        assertEquals(user.getLogin(), userCreated.getLogin());
        assertEquals(user.getName(), userCreated.getName());
        assertEquals(user.getBirthday(), userCreated.getBirthday());
    }

    @Test
    void putTest() {
        User user = userController.findUserById(1);
        User userUpdated = userController.put(new User(1,
                "user_update@yandex.ru",
                "user_update",
                "name_update",
                LocalDate.of(2003, 03, 03)));
        assertEquals(user.getId(), userUpdated.getId());
        assertNotEquals(user.getEmail(), userUpdated.getEmail());
        assertNotEquals(user.getLogin(), userUpdated.getLogin());
        assertNotEquals(user.getName(), userUpdated.getName());
        assertNotEquals(user.getBirthday(), userUpdated.getBirthday());
    }

    @Test
    void findUserByIdTest() {
        User user = userController.findUserById(1);
        assertEquals("user_update@yandex.ru", user.getEmail());
        assertEquals("user_update", user.getLogin());
        assertEquals("name_update", user.getName());
        assertEquals(LocalDate.of(2003, 03, 03), user.getBirthday());
    }

    @Test
    void addFriendTest() {
        User user = userController.findUserById(1);
        assertEquals(0, user.getFriends().size());
        userController.addFriend(1, 2);
        User userWithFriend = userController.findUserById(1);
        assertEquals(1, userWithFriend.getFriends().size());
    }

    @Test
    void deleteFriendTest() {
        User userTest1 = userController.create(new User(0,
                "user1_test@gmail.com",
                "user1",
                "name1",
                LocalDate.of(2001, 01,01)));
        User userTest2 = userController.create(new User(0,
                "user2_test@gmail.com",
                "user2",
                "name2",
                LocalDate.of(2002, 01,01)));
        assertEquals(0, userTest1.getFriends().size());
        userController.addFriend(userTest1.getId(), userTest2.getId());
        userTest1 = userController.findUserById(userTest1.getId());
        assertEquals(1, userTest1.getFriends().size());
        userController.deleteFriend(userTest1.getId(), userTest2.getId());
        User userWithoutFriend = userController.findUserById(userTest1.getId());
        assertEquals(0, userWithoutFriend.getFriends().size());
    }

    @Test
    void getAllFriends() {
        User user = userController.findUserById(2);
        assertEquals(0, user.getFriends().size());
        userController.addFriend(2, 3);
        userController.addFriend(2, 4);
        List<User> userList = userController.getAllFriends(2);
        assertEquals(2, userList.size());
        assertEquals(3, userList.get(0).getId());
        assertEquals(4, userList.get(1).getId());
    }

    @Test
    void getCommonFriends() {
        userController.addFriend(3, 2);
        userController.addFriend(3, 1);
        userController.addFriend(4, 2);
        userController.addFriend(4, 1);
        List<User> userList = userController.getCommonFriends(3, 4);
        assertEquals(2, userList.size());
    }
}