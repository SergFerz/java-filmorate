package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Review;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorService {

    private final DirectorDao directorDao;

    public List<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    public Director createDirector(Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Введено некорректное значение name");
        }
        return directorDao.createDirector(director);
    }

    public Director updateDirector(Director director) {
        getDirectorById(director.getId());
        return directorDao.updateDirector(director);
    }

    public Director getDirectorById( long id) {
        return directorDao.getDirectorById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение director_id"));
    }

    public boolean deleteDirectorById(long id) {
        getDirectorById(id);
        return directorDao.deleteDirectorById(id);
    }
}
