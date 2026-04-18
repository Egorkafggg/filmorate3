package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/films")
@Validated
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("200");
        return film;
    }

    @PutMapping
    @Validated(NotNull.class)
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм {} обновлен", film.getName());
            return film;
        } else {
            log.debug("Фильм {} не был обновлен", film.getName());
            throw new ValidationException("Не получилось обновить фильм");
        }
    }

    private int getNextId() {
        return films.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0) + 1;
    }
}
