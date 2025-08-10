package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.debug("POST /users с телом: {}", user);
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("PUT /users с телом: {}", user);
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("GET /users");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable long userId) {
        log.debug("GET /users/{}", userId);
        return userService.get(userId);
    }
}
