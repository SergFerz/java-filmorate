package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MpaDao {
    List<Mpa> getAllMpa();
    Optional<Mpa> getMpaById(int id);
}
