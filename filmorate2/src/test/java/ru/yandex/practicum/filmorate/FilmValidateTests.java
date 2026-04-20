package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmValidateTests {
    private static FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void validatePostTestDescriptionMaxSymbols() {
        int id = 1;
        Film film = new Film(1L, "test film", "Java - яп", LocalDate.of(2004, 1, 120), 1200);
        filmController.create(film);
        Collection<Film> films = filmController.getAll();
        assertFalse(films.isEmpty());
    }
}
