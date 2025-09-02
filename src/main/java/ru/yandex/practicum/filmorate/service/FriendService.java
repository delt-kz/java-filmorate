package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.repository.user.UserStorage;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserStorage storage;

    public void add(long userId, long friendId) {
        User user = Optional.ofNullable(storage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        User friend = Optional.ofNullable(storage.get(friendId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + friendId + " не существует"));
        if (userId == friendId) {
            throw new IllegalArgumentException("Нельзя добавить себя в друзья");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void delete(long userId, long friendId) {
        User user = Optional.ofNullable(storage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        User friend = Optional.ofNullable(storage.get(friendId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + friendId + " не существует"));
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Set<User> get(long userId) {
        User user = Optional.ofNullable(storage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        return user.getFriends().stream()
                .map(storage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommon(long userId, long otherId) {
        User user = Optional.ofNullable(storage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
            User other = Optional.ofNullable(storage.get(otherId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + otherId + " не существует"));
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(storage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
