package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Long> likes;

    @Override
    public int compareTo(Film o) {
        return this.getLikes().size() - o.getLikes().size();
    }
}
