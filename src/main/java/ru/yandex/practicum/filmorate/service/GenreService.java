package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.repository.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreStorage;

    public Genre get(long genreId) {
        return genreStorage.get(genreId).orElseThrow(() -> new NotFoundException("Жанра с id=" + genreId + " не существует"));
    }

    public List<Genre> findAll() {
        return genreStorage.getAll();
    }
}
