package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private final int id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDateTime birthday;

}
