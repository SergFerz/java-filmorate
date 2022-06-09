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

    Set<Long> likes = new HashSet<>();

    public int getRate() {
        return likes.size();
    }

    public void addLikes(Long like) {
        likes.add(like);
    }

    public void deleteLike(Long like) {
        likes.remove(like);
    }

    @Override
    public int compareTo(Film o) {
        return -(this.getRate() - o.getRate());
    }
}
