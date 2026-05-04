package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public Set<User> getFriends(Long userId) {
        User user = findUser(userId);
        return userStorage.getFriends(user);
    }

    public User create(User user) {
        Set<String> errors = validate(user);
        if (!errors.isEmpty()) {
            log.error("Валидация фильма не пройдена {}", user);
            throw new ValidationException(String.join(", ", errors));
        }
        userStorage.save(user);
        log.info("Пользователь успешно добавлен", user.getId());
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error("Id не указан: {}", newUser);
            throw new ValidationException(String.join("ID должен быть указан"));
        }
        Set<String> errors = validate(newUser);

        if (!errors.isEmpty()) {
            log.error("Валидация пользователья не пройдена: {}", newUser);
            throw new ValidationException(String.join(",", errors));
        }
        User oldUser = findUser(newUser.getId());
        updateUserFields(oldUser, newUser);
        log.info("Пользователь успешно Обновлен: {}", newUser.getId());
        return userStorage.save(oldUser);
    }

    public boolean delete(Long id) {
        log.info("Пользователь успешно удален: {}", id);
        return userStorage.delete(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        userStorage.addFriend(user, friend);
        log.info("Пользователь {} Добавил в друзья{}", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        boolean wereFriend = user.getFriends().contains(friend.getId());

        if (wereFriend) {
            userStorage.deleteFriend(user, friend);
            log.info("Пользователь {} удалил друга {}", user.getId(), friend.getId());
        } else {
            log.warn("Пользрватель {} и {} не были друзьями", user.getId(), friend.getId());
        }
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        User user = findUser(userId);

        User otherUser = findUser(otherId);
        return userStorage.getCommonFriends(user, otherUser);
    }

    public User findUser(Long id) {
        return userStorage.findById(id).orElseThrow(() -> {
            log.error("Id пользователя не найден: {}", id);
            return new NotFoundException("Пользователь не найден");
        });
    }

    public void updateUserFields(User oldUser, User newUser) {
        if (!newUser.getEmail().isBlank()) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin().isBlank()) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (!newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
    }

    private Integer getLikesCount(Film film) {
        return film.getLikes() != null ? film.getLikes().size() : 0;
    }

    private Set<String> validate(User user) {
        Set<String> errors = new HashSet<>();
        if (!isValidEmail(user.getEmail())) {
            errors.add("Электронная почта не может быть пустой и должна содержать @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errors.add("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            errors.add("дата рождения не модеть быть больше текущей даты");
        }
        return errors;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@");
    }
}

