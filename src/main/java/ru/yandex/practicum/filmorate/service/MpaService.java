package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        Mpa mpa = mpaDao.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        return mpa;
    }
}
