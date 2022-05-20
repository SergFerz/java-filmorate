package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

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
    public User create(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Integer id) {
        return userStorage.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
