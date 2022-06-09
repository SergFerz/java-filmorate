package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Value
public class User {

    @NonFinal
    @Setter
    long id;

    @Email
    String email;

    @NotEmpty
    String login;

    @NonFinal
    @Setter
    String name;

    @Past
    LocalDate birthday;

    @NotNull
    Set<Long> friends = new HashSet<>();

    public void addFriend(long id) {
        friends.add(id);
    }

    public void deleteFriend(long id) {
        friends.remove(id);
    }
}
