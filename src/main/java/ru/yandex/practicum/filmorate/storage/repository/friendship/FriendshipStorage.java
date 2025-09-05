package ru.yandex.practicum.filmorate.storage.repository.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendshipStorage {
    boolean add(long userId, long friendId);
    boolean delete(long userId, long friendId);
    List<Long> get(long userId);
    List<Long> getCommon(long userId, long otherId);
}
