package filmorateTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.InMemoryUserStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidateTests {
    private static UserController userController;
    private static UserStorage userStorage;
    private static UserService userService;

    @BeforeEach
    void beforEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void validatePostTestEmailIsNull() {
        User user = new User(1L, "", "Login", "Name", LocalDate.of(2000, 1, 2), new HashSet<>());
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void validatePostTestEmailNotAt() {
        User user = new User(1L, "mail.com", "Login", "Name", LocalDate.of(2000, 1, 2), new HashSet<>());
        assertThrows(ValidationException.class, () -> userController.create(user));
    }
}
