package ru.yandex.practicum.filmorate.storage.user;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void put(Long id, User user) {
        users.put(id, user);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public long getMaxId() {
        return users.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
    }

}
