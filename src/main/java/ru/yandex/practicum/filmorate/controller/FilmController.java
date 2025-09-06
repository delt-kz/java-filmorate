package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film add(@Valid @RequestBody CreateFilmDto film) {
        log.debug("POST /films с телом: {}", film);
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody UpdateFilmDto film) {
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

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("PUT /films/{}/like/{} -> filmId={}, userId={}", filmId, userId, filmId, userId);
        likeService.like(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void dislike(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("DELETE /films/{}/like/{} -> filmId={}, userId={}", filmId, userId, filmId, userId);
        likeService.dislike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.debug("GET /popular");
        return filmService.getPopular(count);
    }
}
