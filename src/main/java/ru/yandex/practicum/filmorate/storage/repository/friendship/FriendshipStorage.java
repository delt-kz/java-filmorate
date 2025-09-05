package ru.yandex.practicum.filmorate.storage.repository.friendship;

import java.util.List;

public interface FriendshipStorage {
    boolean add(long userId, long friendId);

    boolean delete(long userId, long friendId);

    List<Long> get(long userId);

    List<Long> getCommon(long userId, long otherId);
}
