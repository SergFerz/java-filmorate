package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Collection<Genre> getAllGenres();
    Optional<Genre> getGenreById(int id);
}
