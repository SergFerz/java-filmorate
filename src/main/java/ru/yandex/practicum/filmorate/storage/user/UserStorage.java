package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAllUsers();

    User create(User user);

    User update(User user);

    Optional<User> getUserById(long id);

    void addFriend(long idUser, long idFriend, String status);

    void deleteFriend(long idUser, long idFriend);

    void deleteUser(long id);
}
