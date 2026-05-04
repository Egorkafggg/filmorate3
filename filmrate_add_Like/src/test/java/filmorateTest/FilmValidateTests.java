package filmorateTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.InMemoryFilmStorage;
import ru.yandex.practicum.storage.InMemoryUserStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmValidateTests {
    private static FilmController filmController;
    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void validateTestDescriptionMaxSymbols() {
        Film film = new Film(1L, "test film", "Java - cool prog language", LocalDate.of(2004, 1, 1), 120L, new HashSet<>());
        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());
    }

    @Test
    void validatePostTestEarlyDateFilm() {
        Film film = new Film(1L, "first film", "description of first film", LocalDate.of(1895, 12, 28), 120L, new HashSet<>());
        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());
    }

    @Test
    void validatePostTestNullDuration() {
        Film film = new Film(1l, "test Film", "description of test film", LocalDate.of(2004, 1, 1), 0L, new HashSet<>());
        filmController.create(film);
        Collection<Film> films = filmController.findAll();
        assertFalse(films.isEmpty());
    }
}
