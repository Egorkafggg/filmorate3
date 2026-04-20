package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        int newId = getNextId();
        user.setId(newId);
        setName(user);
        users.put(newId, user);
        log.debug("Пользователь {} (ID: {}) создан", user.getName(), newId);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new ValidationException("ID пользователя не может быть null при обновлении");

        }
        setName(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь '{}'(ID:{}) обноавлен", user.getName(), user.getId());
            return user;
        } else {
            log.warn("Попытка обновить несуществующего пользователя с ID:{}", user.getId());
            throw new ValidationException("Пользователь с ID" + user.getId() + "не найден");
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
            if (user.getLogin() != null && !user.getLogin().isBlank()) {
                user.setName(user.getLogin());
            } else {
                throw new ValidationException("Имя и логин не могут быть одновременно пустыми");
            }
        }
    }
}
