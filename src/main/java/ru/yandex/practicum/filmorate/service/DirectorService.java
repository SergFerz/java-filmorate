package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

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
        Optional<Director> director = directorDao.getDirectorById(id);
        if (director.isEmpty()) {
            log.warn("director с идентификатором {} не найден.", id);
            throw  new NotFoundException("Введено некорректное значение director_id");
        }
        return director.get();
    }

    public boolean deleteDirectorById(long id) {
        getDirectorById(id);
        return directorDao.deleteDirectorById(id);
    }
}
