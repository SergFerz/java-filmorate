package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Film {

    private final int id;
    private final String name;
    private final String description;
    private final LocalDateTime releaseDate;
    private final int duration;
}
