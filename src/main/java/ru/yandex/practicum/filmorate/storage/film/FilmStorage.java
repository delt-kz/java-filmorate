package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film get(Long id);
    Collection<Film> findAll();
    void put(Long id, Film film);
    void delete(Long id);
    long getMaxId();
}
