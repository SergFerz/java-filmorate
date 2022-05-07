package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        if (user == null) {
            log.debug("Введено некорректное значение null");
            throw new ValidationException("Введено некорректное значение null");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Введен некорректный адрес электронной почты");
            throw new ValidationException("Введен некорректный адрес электронной почты");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Введен некорректный логин");
            throw new ValidationException("Введен некорректный логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Введен некорректный день рождения");
            throw new ValidationException("Введен некорректный день рождения");
        } else if (user.getId() < 0) {
            log.debug("Некорректный идентификатор id");
            throw new ValidationException("Некорректный идентификатор id");
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.debug("Добавлен новый пользователь");
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/users")
    public void put(@RequestBody User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null) {
            log.debug("В переданных данных отсутствует адрес электронной почты");
            throw new ValidationException("В переданных данных отсутствует адрес электронной почты");
        } else {
            log.debug("Пользователь " + user.getId() + " обновлен.");
            users.put(user.getId(), user);
        }
    }
}
