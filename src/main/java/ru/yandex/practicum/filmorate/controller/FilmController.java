package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film add(@Valid @RequestBody Film film) {
        log.debug("POST /films с телом: {}", film);
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("PUT /films с телом: {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("GET /films");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film get(@PathVariable long filmId) {
        log.debug("GET /films/{}", filmId);
        return filmService.get(filmId);
    }
}
