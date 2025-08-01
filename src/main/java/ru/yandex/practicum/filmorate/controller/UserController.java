package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.debug("POST /users с телом: {}", user);
        if (users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()))) {
            log.trace("Попытка добавить существующий email: {}", user.getEmail());
            throw new ValidationException("Невозможно добавить пользователя с существующим адресом электронной почты");
        }
        ensureNameIsNotBlank(user);
        user.setId(getNextId());
        log.trace("User ID: {}", user.getId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("PUT /users с телом: {}", user);
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new NotFoundException("Указан неверный id пользователя");
        }
        ensureNameIsNotBlank(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("GET /users");
        return users.values();
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void ensureNameIsNotBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя пустое, подставляем логин как имя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

}
