package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User add(User user) {
        if (storage.findAll().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()))) {
            throw new ValidationException(
                    "Невозможно добавить пользователя с существующим адресом электронной почты: " + user.getEmail());
        }
        ensureNameIsNotBlank(user);
        user.setId(storage.getMaxId()+1);
        log.trace("User ID: {}", user.getId());
        storage.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }
        if (storage.get(user.getId()) == null) {
            throw new NotFoundException("Указан id несуществующего пользователя: " + user.getId());
        }
        ensureNameIsNotBlank(user);
        storage.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User get(long id) {
        User user = storage.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь по id:" + id + " не найден");
        }
        return user;
    }

    private void ensureNameIsNotBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя пустое, подставляем логин как имя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
