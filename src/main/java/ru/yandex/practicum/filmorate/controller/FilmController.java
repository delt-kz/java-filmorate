package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("POST /films с телом: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        log.trace("Film ID: {}", film.getId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("PUT /films с телом: {}", film);
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new NotFoundException("Указан неверный id фильма");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("GET /films");
        return films.values();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Попытка добавить фильм с пустым названием: {}", film);
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Попытка добавить фильм с описанием более 200 символов: {}", film);
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Попытка добавить фильм с датой релиза раньше 28.12.1895: {}", film);
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Попытка добавить фильм с отрицательной продолжительностью: {}", film);
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
