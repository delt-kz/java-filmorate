package ru.yandex.practicum.filmorate.storage.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> get(Long id);

    Collection<User> findAll();

    User put(User user);

    boolean delete(Long id);

    boolean update(User user);
}
