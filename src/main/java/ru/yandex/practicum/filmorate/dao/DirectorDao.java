package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {

    List<Director> getAllDirectors();

    Director createDirector(Director director);

    Director updateDirector(Director director);

    Optional<Director> getDirectorById(long id);

    boolean deleteDirectorById(long id);
}
