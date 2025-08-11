package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void like(long filmId, long userId) {
        Film film = Optional.ofNullable(filmStorage.get(filmId))
                .orElseThrow(() -> new NotFoundException("Фильм c id " + filmId + " не существует"));
        User user = Optional.ofNullable(userStorage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));

        film.getLikes().add(userId);
    }

    public void dislike(long filmId, long userId) {
        Film film = Optional.ofNullable(filmStorage.get(filmId))
                .orElseThrow(() -> new NotFoundException("Фильм c id " + filmId + " не существует"));
        // validate that user exists
        Optional.ofNullable(userStorage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));
        film.getLikes().remove(userId);
    }
}
