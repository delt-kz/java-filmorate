package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.repository.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.repository.user.UserStorage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    public void add(long userId, long friendId) {
        User user = userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        User friend = userStorage.get(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + friendId + " не существует"));
        if (userId == friendId) {
            throw new IllegalArgumentException("Нельзя добавить себя в друзья");
        }
        friendshipStorage.add(userId, friendId);
    }

    public void delete(long userId, long friendId) {
        User user = userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        User friend = userStorage.get(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + friendId + " не существует"));
        friendshipStorage.delete(userId, friendId);
    }

    public Set<User> get(long userId) {
        User user = userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        return friendshipStorage.get(userId).stream()
                .map(userStorage::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommon(long userId, long otherId) {
        User user = userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
            User other = userStorage.get(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + otherId + " не существует"));
        return friendshipStorage.getCommon(userId, otherId).stream()
                .map(userStorage::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }
}
