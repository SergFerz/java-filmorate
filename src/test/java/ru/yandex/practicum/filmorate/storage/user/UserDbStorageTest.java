package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    void getAllUsersTest() {
        Collection<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void createUserTest() {
        User user = new User(0,
                "user1_test@gmail.com",
                "user1",
                "name1",
                LocalDate.of(2001, 01,01));
        User userCreated = userStorage.create(user);
        assertNotEquals(user.getId(), userCreated.getId());
        assertEquals(user.getEmail(), userCreated.getEmail());
        assertEquals(user.getLogin(), userCreated.getLogin());
        assertEquals(user.getName(), userCreated.getName());
        assertEquals(user.getBirthday(), userCreated.getBirthday());
    }

    @Test
    void updateUserTest() {
        User user = userStorage.getUserById(1).get();
        User userUpdated = userStorage.update(new User(1,
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
    void getUserByIdTest() {
        User user = userStorage.getUserById(1).get();
        assertEquals("user_update@yandex.ru", user.getEmail());
        assertEquals("user_update", user.getLogin());
        assertEquals("name_update", user.getName());
        assertEquals(LocalDate.of(2003, 03, 03), user.getBirthday());
    }

    @Test
    void addFriendTest() {
        User user = userStorage.getUserById(1).get();
        assertEquals(0, user.getFriends().size());
        userStorage.addFriend(1, 2, "UNCONFIRM");
        User userWithFriend = userStorage.getUserById(1).get();
        assertEquals(1, userWithFriend.getFriends().size());
    }

    @Test
    void deleteFriendTest() {
        User userWithFriend = userStorage.getUserById(1).get();
        assertEquals(1, userWithFriend.getFriends().size());
        userStorage.deleteFriend(1, 2);
        User userWithoutFriend = userStorage.getUserById(1).get();
        assertEquals(0, userWithoutFriend.getFriends().size());
    }
}