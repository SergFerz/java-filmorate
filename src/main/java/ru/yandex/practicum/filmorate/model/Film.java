package ru.yandex.practicum.filmorate.model;

import lombok.Value;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class Film implements Comparable<Film> {

     Integer id;
     String name;
     String description;
     LocalDate releaseDate;
     int duration;

     Set<Integer> likes = new TreeSet<Integer>();

    @Override
    public int compareTo(Film o) {
        return this.getLikes().size() - o.getLikes().size();
    }
}
