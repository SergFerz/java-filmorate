package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    User create(User user);

    User update(User user);

    Optional<User> getUserById(long id);

    User addFriend(long idUser, long idFriend, String status);


    public User deleteFriend(long idUser, long idFriend);
}
