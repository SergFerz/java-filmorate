package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class Film implements Comparable<Film> {

     long id;
     String name;
     String description;
     LocalDate releaseDate;
     int duration;

     Set<Long> likes = new TreeSet<>();

    @Override
    public int compareTo(Film o) {
        return this.getLikes().size() - o.getLikes().size();
    }
}
