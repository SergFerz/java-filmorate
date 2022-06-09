package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return (List<User>) userStorage.getAllUsers();
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        getUserById(user.getId());
        validateUser(user);
        return userStorage.update(user);
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return user;
    }

    public User addFriend(long idUser, long idFriend) {
        User user = getUserById(idUser);
        User friend = getUserById(idFriend);
        user.addFriend(idFriend);
        friend.addFriend(idUser);
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public User deleteFriend(long idUser, long idFriend) {
        User user = getUserById(idUser);
        User friend = getUserById(idFriend);
        user.deleteFriend(idFriend);
        friend.deleteFriend(idUser);
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public List<User> getAllFriends(long id) {
        return getUserById(id).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser1, long idUser2) {
        User user1 = getUserById(idUser1);
        User user2 = getUserById(idUser2);
        Set<Long> commonFriendsId = new HashSet<>(user1.getFriends());
        commonFriendsId.retainAll(user2.getFriends());
        return commonFriendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private User validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.debug("Введен некорректный логин");
            throw new ValidationException("Введен некорректный логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Введен некорректный день рождения");
            throw new ValidationException("Введен некорректный день рождения");
        }
        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
//for review