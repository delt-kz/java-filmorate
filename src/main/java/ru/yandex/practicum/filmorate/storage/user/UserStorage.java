package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
        User get(Long id);
        Collection<User> findAll();
        void put(Long id, User user);
        void delete(Long id);
        long getMaxId();
}
