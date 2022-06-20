package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

@Component
@Slf4j
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Like addLike(long idFilm, long idUser) {
        Like like = new Like(idFilm, idUser);
        jdbcTemplate.update("INSERT INTO likes(film_id, user_id) VALUES (?, ?);", idFilm, idUser);
        return like;
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", idFilm, idUser);
    }
}
