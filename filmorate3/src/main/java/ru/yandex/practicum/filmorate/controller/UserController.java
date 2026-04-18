package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(getNextId());
        setName(user);
        users.put(user.getId(), user);
        log.debug("Пользователь {} создан");
        return user;
    }

    @PutMapping
    @Validated(NotNull.class)
    public User update(@Valid @RequestBody User user) {
        setName(user);

        if (users.containsKey(user.getId())) {
            setName(user);
            users.put(user.getId(), user);
            log.debug("Пользователь {} обновлен", user.getName());
            return user;
        } else {
            log.debug("Пользователь {} не был добавлен", user.getName());
            throw new ValidationException("Попытка обновления пользователя не удачна");

        }
    }

    private int getNextId() {
        return users.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0) + 1;
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}