package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Data
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long counterId = 1L;

    public long getNextId() {
        while (users.containsKey(counterId)) {
            counterId++;
        }
        return counterId;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        List<User> userList = new ArrayList<>(users.values());
        return userList;
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь");
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Пользователь " + user.getId() + " обновлен.");
        return user;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User addFriend(long idUser, long idFriend, String status) {
        User user = getUserById(idUser).get();
        User friend = getUserById(idFriend).get();
        user.addFriend(idFriend);
        friend.addFriend(idUser);
        update(user);
        update(friend);
        return user;
    }

    @Override
    public User deleteFriend(long idUser, long idFriend) {
        User user = getUserById(idUser).get();
        User friend = getUserById(idFriend).get();
        user.deleteFriend(idFriend);
        friend.deleteFriend(idUser);
        update(user);
        update(friend);
        return user;
    }
}
