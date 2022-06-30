package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeDao {

    Like addLike(long idFilm, long idUser);

    void deleteLike(long idFilm, long idUser);

    void deleteAllLikesFilm(long idFilm);
}
