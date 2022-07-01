package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Value
public class Film implements Comparable<Film> {

    @NonFinal
    @Setter
    long id;

    @NotEmpty
    String name;

    LocalDate releaseDate;

    @NotEmpty
    String description;

    int duration;

    @NonFinal
    @Setter
    int rate;

    Mpa mpa;

    @NonFinal
    @Setter
    Set<Genre> genres;

    @NonFinal
    @Setter
    Set<Long> likes;

    @Override
    public int compareTo(Film o) {
        return o.getLikes().size() - this.likes.size();
    }
}
