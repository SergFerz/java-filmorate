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
}
