package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();
    private Long counter = 1L;

    public Long getNextId() {
        while (users.containsKey(counter)) {
            counter++;
        }
        return counter;
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User create(User user) {
        validateUser(user);
        if (user.getName().isBlank()) {
                user.setName(user.getLogin());
        }
        log.debug("Добавлен новый пользователь");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUser(user);
        if (!users.containsValue(user)) {
            throw new NullPointerException("Этот film не содержится в реестре");
        }
        log.debug("Пользователь " + user.getId() + " обновлен.");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Long id) {
        if ((id < 1) || (id == null))  {throw new NullPointerException("Введено некорректное значение id");}
        return users.get(id);
    }

    private void validateUser(User user) {
        if (user == null) {
            log.debug("Введено некорректное значение null");
            throw new ValidationException("Введено некорректное значение null");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")|| user.getEmail() == null) {
            log.debug("Введен некорректный адрес электронной почты");
            throw new ValidationException("Введен некорректный адрес электронной почты");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Введен некорректный логин");
            throw new ValidationException("Введен некорректный логин");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Введен некорректный день рождения");
            throw new ValidationException("Введен некорректный день рождения");
        } else if (user.getId() == null || user.getId() < 0) {
            log.debug("Некорректный идентификатор id");
            throw new NullPointerException("Некорректный идентификатор id");
        }
    }
}
