package ru.yandex.practicum.filmorate.storage.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> get(Long id);

    Collection<Film> findAll();

    Film put(Film film);

    boolean delete(Long id);

    boolean update(Film film);
}
