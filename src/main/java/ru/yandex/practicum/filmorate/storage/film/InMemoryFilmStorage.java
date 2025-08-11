package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public void put(Long id, Film film) {
        films.put(id, film);
    }

    @Override
    public void delete(Long id) {
        films.remove(id);
    }

    @Override
    public long getMaxId() {
        return films.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
    }

}
