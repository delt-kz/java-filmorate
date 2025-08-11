package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Film add(Film film) {
        film.setId(storage.getMaxId() + 1);
        log.debug("Film ID: {}", film.getId());
        storage.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Не указан id фильма");
        }
        if (storage.get(film.getId()) == null) {
            throw new NotFoundException("Указан id несуществующего фильма: " + film.getId());
        }
        storage.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film get(long id) {
        Film film = storage.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм по id:" + id + " не найден");
        }
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Отрицательное значение count");
        }
        log.trace("Получены {} популярных постов", count);
        return storage.findAll().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
