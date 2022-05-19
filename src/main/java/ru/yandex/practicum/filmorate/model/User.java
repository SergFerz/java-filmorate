package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private final Long id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private Set<Long> friends;
}
