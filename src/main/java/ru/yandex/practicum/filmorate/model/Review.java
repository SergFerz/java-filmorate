package ru.yandex.practicum.filmorate.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public class Review {

    @NonFinal
    @Setter
    Long id;
    Long userId;
    Long filmId;
    @NonFinal
    @Setter
    String content;
    @NonFinal
    @Setter
    Boolean isPositive;
    @NonFinal
    @Setter
    Integer useful = 0;
}