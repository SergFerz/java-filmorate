package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserStorage userStorage;

    public User addFriend(long idUser1, long idUser2) {
        if (idUser1 < 0 || idUser2 < 0) {
            throw new ValidationException("Введено некорректное значение id");}
        User user1 = userStorage.getById(idUser1);
        User friend = userStorage.getById(idUser2);
        user1.addFriend(idUser2);
        friend.addFriend(idUser1);
        userStorage.update(user1);
        userStorage.update(friend);
        return user1;
    }

    public User deleteFriend(long idUser1, long idUser2){
        if (idUser1 < 0 || idUser2 < 0) {
            throw new ValidationException("Введено некорректное значение id");}
        User user1 = userStorage.getById(idUser1);
        User friend = userStorage.getById(idUser2);
        user1.deleteFriend(idUser2);
        friend.deleteFriend(idUser1);
        userStorage.update(user1);
        userStorage.update(friend);
        return user1;
    }

    public List<User> getAllFriends(long id) {
        if (id < 0) {
            throw new ValidationException("Введено некорректное значение id");}
       return userStorage.getById(id).getFriends().stream()
               .map(userStorage::getById)
               .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser1, long idUser2) {
        if (idUser1 < 0 || idUser2 < 0) {
            throw new NullPointerException("Введено некорректное значение id");
        }
        try {
            User user1 = userStorage.getById(idUser1);
            User user2 = userStorage.getById(idUser2);
            Set<Long> commonFriendsId = user1.getFriends();
            commonFriendsId.retainAll(user2.getFriends());
            return commonFriendsId.stream()
                    .map(userStorage::getById)
                    .collect(Collectors.toList());
        } catch (NullPointerException ex) {
            return new ArrayList<User>();
        }

    }
}
