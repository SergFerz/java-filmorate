package ru.yandex.practicum.filmorate.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class Film implements Comparable<Film> {

    @NotNull
    @NotBlank
    @Positive
    Long id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    String description;
    @NotNull
    @NotBlank
    LocalDate releaseDate;
    @NotNull
    @NotBlank
    @Positive
    int duration;

     Set<Long> likes = new TreeSet<Long>();

    @Override
    public int compareTo(Film o) {
        return this.getLikes().size() - o.getLikes().size();
    }
}
