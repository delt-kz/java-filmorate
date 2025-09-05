package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(CreateFilmDto createFilm) {
        Film film = new Film();
        film.setName(createFilm.getName());
        film.setDescription(createFilm.getDescription());
        film.setReleaseDate(createFilm.getReleaseDate());
        film.setDuration(createFilm.getDuration());
        film.setMpa(createFilm.getMpa());
        film.setGenres(createFilm.getGenres());
        return film;
    }

    public static Film updateFilmFields(UpdateFilmDto updateFilm, Film film) {
        if (updateFilm.hasName()) {
            film.setName(updateFilm.getName());
        }

        if (updateFilm.hasDescription()) {
            film.setDescription(updateFilm.getDescription());
        }

        if (updateFilm.hasReleaseDate()) {
            film.setReleaseDate(updateFilm.getReleaseDate());
        }

        if (updateFilm.hasDuration()) {
            film.setDuration(updateFilm.getDuration());
        }

        if (updateFilm.hasRating()) {
            film.setMpa(updateFilm.getMpa());
        }

        if (updateFilm.hasGenres()) {
            film.setGenres(updateFilm.getGenres());
        }
        return film;
    }
}
