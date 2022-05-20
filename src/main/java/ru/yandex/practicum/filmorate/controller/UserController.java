package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {

        return userStorage.create(user);
    }

    @PutMapping("/users")
    public User put(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("/users/{id}")
    public User findUserById(@Valid @PathVariable long id) {
        return userStorage.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable long id, long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @PathVariable long id, long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@Valid @PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PathVariable long id, long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
