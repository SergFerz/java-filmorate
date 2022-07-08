package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface LikeDao {

    Like addLike(long idFilm, long idUser);

    int deleteLike(long idFilm, long idUser);

    Map<Long, Set<Long>> getLikesForAllFilms();

    Map<Long, Map<Long, Double>> buildDifferencesMatrix();

    Set<Long> getLikesByFilmId(long filmId);
}
