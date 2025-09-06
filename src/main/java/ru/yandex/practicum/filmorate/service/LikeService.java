package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.repository.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.repository.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.repository.user.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDbStorage likeStorage;

    public void like(long filmId, long userId) {
        filmStorage.get(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм c id " + filmId + " не существует"));
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));

        likeStorage.add(filmId, userId);
    }

    public void dislike(long filmId, long userId) {
        filmStorage.get(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм c id " + filmId + " не существует"));
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя c id " + userId + " не существует"));

        likeStorage.delete(filmId, userId);
    }

    public List<Long> getFilmLikes(long filmId) {
        return likeStorage.getFilmLikes(filmId);
    }
}
