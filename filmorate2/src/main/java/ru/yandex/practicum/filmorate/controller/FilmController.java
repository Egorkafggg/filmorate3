package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
        int newId = getNextId();
        film.setId(newId);

        if (films.containsKey(newId)) {
            throw new ValidationException("Film with ID " + newId + " already exists");
        }

        films.put(newId, film);
        log.info("Film '{}' created successfully with ID: {}", film.getName(), newId);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Film ID cannot be null for update");
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Attempt to update non-existent film with ID: {}", film.getId());
            throw new ValidationException("Film with ID" + film.getId() + "not found");
        }

        films.put(film.getId(), film);
        log.info("Film '{}' (ID: {}) updated successfully", film.getName(), film.getId());
        return film;
    }

    private int getNextId() {
        return films.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0) + 1;
    }
}
