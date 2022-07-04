package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Value;

@Value
public class Like {
    long filmId;
    long userId;
}
