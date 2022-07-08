package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface LikeDao {

    Like addLike(long idFilm, long idUser);

    int deleteLike(long idFilm, long idUser);

    /**
     * Метод возвращает ассоциативный массив, где ключом является идентификатор фильма, а значением - набор
     * идентификаторов пользователей, поставивших лайки этому фильму. Полученный массив будет содержать только фильмы,
     * удовлетворяющие заданным параметрам genreId, year.
     *
     * @param genreId   идентификатор жанра фильма;
     * @param year      год выпуска фильма;
     * @return ассоциативный массив, где ключом является идентификатор фильма, а значением - набор идентификаторов
     *         пользователей, поставивших лайки этому фильму
     */
    Map<Long, Set<Long>> getLikesForFilteredFilms(Optional<Integer> genreId, Optional<Integer> year);

    Map<Long, Set<Long>> getLikesForAllFilms();

    Map<Long, Map<Long, Double>> buildDifferencesMatrix();

    Set<Long> getLikesByFilmId(long filmId);
}
