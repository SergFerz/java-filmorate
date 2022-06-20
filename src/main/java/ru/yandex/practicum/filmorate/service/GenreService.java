package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreDao genreDao;

    public List<Genre> getAllGenres() {
        return (List<Genre>) genreDao.getAllGenres();
    }

    public Genre getGenreById(int id) {
        Genre genre = genreDao.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return genre;
    }
}
