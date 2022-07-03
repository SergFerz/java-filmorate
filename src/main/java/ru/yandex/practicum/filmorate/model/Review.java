package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.HashMap;
import java.util.List;

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
    Integer useful;
    @Getter
    @Setter
    List<Long> likes;
    @Getter
    @Setter
    List<Long> dislikes;
}