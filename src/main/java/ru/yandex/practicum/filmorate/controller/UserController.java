package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody CreateUserDto user) {
        log.debug("POST /users с телом: {}", user);
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody UpdateUserDto user) {
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

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("PUT /{}/friends/{}", userId, friendId);
        friendService.add(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("DELETE /{}/friends/{}", userId, friendId);
        friendService.delete(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(@PathVariable long userId) {
        log.debug("GET /{}/friends", userId);
        return friendService.get(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        log.debug("GET /{}/friends/common/{}", userId, otherId);
        return friendService.getCommon(userId, otherId);
    }
}
