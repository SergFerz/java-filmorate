package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return (List<User>) userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(long idUser, long idFriend) {
        if (idUser < 0 || idFriend < 0) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        User user = userStorage.getUserById(idUser);
        User friend = userStorage.getUserById(idFriend);
        user.addFriend(idFriend);
        friend.addFriend(idUser);
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public User deleteFriend(long idUser, long idFriend) {
        if (idUser < 0 || idFriend < 0) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        User user = userStorage.getUserById(idUser);
        User friend = userStorage.getUserById(idFriend);
        user.deleteFriend(idFriend);
        friend.deleteFriend(idUser);
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public List<User> getAllFriends(long id) {
        if (id < 0) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser1, long idUser2) {
        if (idUser1 < 0 || idUser2 < 0) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        User user1 = userStorage.getUserById(idUser1);
        User user2 = userStorage.getUserById(idUser2);
        Set<Long> commonFriendsId = new HashSet<>(user1.getFriends());
        commonFriendsId.retainAll(user2.getFriends());
        return commonFriendsId.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

    }
}
