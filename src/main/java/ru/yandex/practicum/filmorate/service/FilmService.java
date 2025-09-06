package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.repository.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.repository.film.FilmStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final LikeService likeService;
    private final RatingDbStorage ratingStorage;
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;

    public Film add(CreateFilmDto createFilm) {
        Film film = FilmMapper.mapToFilm(createFilm);

        Set<Long> genreIds = extractGenreIds(film);
        validateGenreIds(genreIds);
        validateRatingId(film.getMpa().getId());

        film = filmStorage.put(film);
        genreStorage.addFilmGenres(film.getId(), genreIds);

        log.debug("Добавлен фильм ID: {}", film.getId());
        log.info("Добавлен фильм: {}", film);

        return film;
    }

    public Film update(UpdateFilmDto updateFilm) {
        if (updateFilm.getId() == null) {
            throw new ValidationException("Не указан id фильма");
        }

        Film film = filmStorage.get(updateFilm.getId())
                .map(destin -> FilmMapper.updateFilmFields(updateFilm, destin))
                .orElseThrow(() ->
                        new NotFoundException("Фильм с id=" + updateFilm.getId() + " не найден"));

        Set<Long> genreIds = extractGenreIds(film);
        validateGenreIds(genreIds);
        validateRatingId(film.getMpa().getId());

        filmStorage.update(film);
        genreStorage.updateFilmGenres(film.getId(), genreIds);

        log.info("Фильм обновлен: {}", film);

        return film;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll().stream()
                .peek(film -> film.setGenres(genreStorage.getFilmGenres(film.getId())))
                .toList();
    }

    public Film get(long id) {
        Film film = filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        return film;
    }

    public Collection<Film> getPopular(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Отрицательное значение count");
        }
        log.trace("Получены {} популярных фильмов", count);
        return filmStorage.getPopular(count);
    }

    private void validateRatingId(Long ratingId) {
        ratingStorage.get(ratingId)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + ratingId + " не существует"));
    }

    private void validateGenreIds(Set<Long> genreIds) {
        for (Long genreId : genreIds) {
            if (genreStorage.get(genreId).isEmpty()) {
                throw new NotFoundException("Жанр с id=" + genreId + " не существует");
            }
        }
    }

    private Set<Long> extractGenreIds(Film film) {
        return film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
    }
}
