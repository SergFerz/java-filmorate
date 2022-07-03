package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Value;

@Value
public class Like {
    private long filmId;
    private long userId;
}
