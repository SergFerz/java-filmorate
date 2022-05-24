package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
        List<User> userList = new ArrayList<>();
        if (!users.isEmpty()) {
            for (User u : users.values())
                userList.add(u);
        }
        return userList;
    }

    @Override
    public User create(User user) {
        validateUser(user);
        log.debug("Добавлен новый пользователь");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUser(user);
        log.debug("Пользователь " + user.getId() + " обновлен.");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        if (id < 1) {
            throw new NotFoundException("Введено некорректное значение id");
        }
        return users.get(id);
    }

    private User validateUser(User user) {
        if (user.getId() < 0) {
            log.debug("Введено некорректное значение id");
            throw new NotFoundException("Введено некорректное значение id");
        } else if (user.getId() == 0) {
            user.setId(getNextId());
        } else if (user.getLogin().contains(" ")) {
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
