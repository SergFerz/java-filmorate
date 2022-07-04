package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(exclude = {"name"})
public class Genre {
    int id;
    String name;
}
