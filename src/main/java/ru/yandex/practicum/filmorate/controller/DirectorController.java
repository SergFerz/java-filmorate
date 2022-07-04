package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@ControllerAdvice
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/directors")
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @PostMapping("/directors")
    public Director createDirector(@Valid @RequestBody Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping("/directors")
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @GetMapping("/directors/{id}")
    public Director getDirectorById(@Valid @PathVariable long id) {
        return directorService.getDirectorById(id);
    }

    @DeleteMapping("/directors/{id}")
    public boolean deleteDirectorById(@Valid @PathVariable long id) {
        return directorService.deleteDirectorById(id);
    }
}
