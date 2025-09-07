package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.repository.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User add(CreateUserDto createUser) {
        if (isEmailExists(createUser.getEmail())) {
            throw new ValidationException(
                    "Невозможно добавить пользователя с существующим адресом электронной почты: " + createUser.getEmail());
        }
        ensureNameIsNotBlank(createUser);
        User user = UserMapper.mapToUser(createUser);
        user = storage.put(user);
        log.debug("User ID: {}", user.getId());
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    public User update(UpdateUserDto updateUser) {
        if (updateUser.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }
        User user = storage.get(updateUser.getId())
                .map(destin -> UserMapper.updateUserFields(updateUser, destin))
                .orElseThrow(() -> new NotFoundException("Указан id несуществующего пользователя"));
        storage.update(user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User get(long id) {
        return storage.get(id).orElseThrow(() -> new NotFoundException("Пользователь по id:" + id + " не найден"));
    }

    private void ensureNameIsNotBlank(CreateUserDto createUser) {
        if (createUser.getName() == null || createUser.getName().isBlank()) {
            log.warn("Имя пользователя пустое, подставляем логин как имя: {}", createUser.getLogin());
            createUser.setName(createUser.getLogin());
        }
    }

    private boolean isEmailExists(String email) {
        return storage.findAll().stream()
                .map(User::getEmail)
                .anyMatch(email::equals);
    }
}
