package ru.yandex.practicum.filmorate.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class Film implements Comparable<Film> {

    @NotNull
    @NotBlank
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
    int duration;

     Set<Long> likes = new TreeSet<Long>();

    @Override
    public int compareTo(Film o) {
        return this.getLikes().size() - o.getLikes().size();
    }
}
