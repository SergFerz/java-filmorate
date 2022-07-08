package ru.yandex.practicum.filmorate.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;


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

    @NonFinal
    @Setter
    Mpa mpa;

    @NonFinal
    @Setter
    Set<Genre> genres;

    @NonFinal
    @Setter
    Set<Director> directors;

    @NonFinal
    @Setter
    Set<Long> likes;

    @Override
    public int compareTo(Film o) {
        return o.getLikes().size() - this.likes.size();
    }
}
