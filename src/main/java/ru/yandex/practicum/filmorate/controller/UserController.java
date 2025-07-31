package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("POST /users с телом: {}", user);
        if (users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()))) {
            log.trace("Попытка добавить существующий email: {}", user.getEmail());
            throw new ValidationException("Невозможно добавить пользователя с существующим адресом электронной почты");
        }
        validateUser(user);
        user.setId(getNextId());
        log.trace("User ID: {}", user.getId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("PUT /users с телом: {}", user);
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new NotFoundException("Указан неверный id пользователя");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("GET /users");
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Попытка создать пользователя с пустым email: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Некорректный email: {}", user.getEmail());
            throw new ValidationException("Электронная почта должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Попытка создать пользователя с пустым логином: {}", user);
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Попытка создать пользователя с логином, содержащим пробелы: {}", user);
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка создать пользователя с датой рождения в будущем: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Имя пользователя пустое, подставляем логин как имя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
