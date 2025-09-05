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
import ru.yandex.practicum.filmorate.storage.repository.GenreFilmDbStorage;
import ru.yandex.practicum.filmorate.storage.repository.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.repository.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final LikeService likeService;

    private final FilmStorage storage;
    private final GenreFilmDbStorage genreFilmDbStorage;
    private final GenreDbStorage genreStorage;
    private final RatingDbStorage ratingStorage;


    public Film add(CreateFilmDto createFilm) {
        Film film = FilmMapper.mapToFilm(createFilm);

        Set<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Long mpaId = film.getMpa().getId();
        ratingStorage.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинга с id=" + mpaId + " не существует"));
        for (Long genreId : genreIds) {
            if (genreStorage.get(genreId).isEmpty()) {
                throw new NotFoundException("Жанра с id=" + genreId + " не существует");
            }
        }

        film = storage.put(film);

        //Подготовка film для возвращения
        film.setMpa(ratingStorage.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинга с id=" + mpaId + " не существует")));
        genreFilmDbStorage.addFilmGenres(film.getId(), genreIds);
        log.debug("Film ID: {}", film.getId());
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film update(UpdateFilmDto updateFilm) {
        if (updateFilm.getId() == null) {
            throw new ValidationException("Не указан id фильма");
        }
        Film film = storage.get(updateFilm.getId())
                .map(destin -> FilmMapper.updateFilmFields(updateFilm, destin))
                .orElseThrow(() -> new NotFoundException("Указан id несуществующего фильма"));

        Set<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        ratingStorage.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинга с id=" + film.getMpa().getId() + " не существует"));
        for (Long genreId : genreIds) {
            if (genreStorage.get(genreId).isEmpty()) {
                throw new NotFoundException("Жанра с id=" + genreId + " не существует");
            }
        }
        storage.update(film);

        film.setMpa(ratingStorage.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинга с id=" + film.getMpa() + " не существует")));
        genreFilmDbStorage.updateFilmGenres(film.getId(), genreIds);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    public Collection<Film> findAll() {
        return storage.findAll().stream()
                .peek(film -> {
                    Set<Long> genreIds = genreFilmDbStorage.getFilmGenres(film.getId());
                    Set<Genre> genres = genreIds.stream()
                            .map(genreStorage::get)
                            .flatMap(Optional::stream)
                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Genre::getId))));
                    film.setGenres(genres);
                })
                .peek(film ->
                    film.setMpa(ratingStorage.get(film.getMpa().getId())
                            .orElseThrow(() -> new NotFoundException("Рейтинг с id " + film.getMpa().getId() + " не существует")))
                )
                .toList();

    }


    public Film get(long id) {
        Film film = storage.get(id).orElseThrow(() -> new NotFoundException("Фильм по id:" + id + " не найден"));
        Set<Long> genreIds = genreFilmDbStorage.getFilmGenres(film.getId());
        Set<Genre> genres = genreIds.stream()
                .map(genreStorage::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Genre::getId))));
        film.setGenres(genres);
        film.setMpa(ratingStorage.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинга с id=" + film.getMpa() + " не существует")));
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Отрицательное значение count");
        }
        log.trace("Получены {} популярных фильмов", count);
        return storage.findAll().stream()
                .sorted(Comparator.comparing(film -> likeService.getFilmLikes(((Film) film).getId()).size()).reversed())
                .limit(count)
                .peek(film -> {
                    Set<Long> genreIds = genreFilmDbStorage.getFilmGenres(film.getId());
                    Set<Genre> genres = genreIds.stream()
                            .map(genreStorage::get)
                            .flatMap(Optional::stream)
                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Genre::getId))));
                    film.setGenres(genres);
                })
                .peek(film ->
                        film.setMpa(ratingStorage.get(film.getMpa().getId())
                                .orElseThrow(() -> new NotFoundException("Рейтинг с id " + film.getMpa().getId() + " не существует")))
                )
                .toList();
    }
}
