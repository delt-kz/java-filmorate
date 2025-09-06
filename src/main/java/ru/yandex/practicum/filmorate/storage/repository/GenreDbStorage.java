package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String ADD_QUERY = "INSERT INTO genres (name) VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE id = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.id, g.name FROM films_genres AS fg" +
            " LEFT JOIN genres AS g ON g.id = fg.genre_id" +
            " WHERE film_id = ?";
    private static final String REMOVE_FILM_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String ADD_GENRES_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)";

    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> get(long genreId) {
        return findOne(FIND_QUERY, genreId);
    }

    public List<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Long add(Genre genre) {
        return insert(ADD_QUERY, genre.getName());
    }

    public boolean delete(long genreId) {
        return delete(DELETE_QUERY, genreId);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return findMany(FIND_FILM_GENRES_QUERY, filmId);
    }

    public void removeFilmGenres(long filmId) {
        delete(REMOVE_FILM_GENRES_QUERY, filmId);
    }

    public void addFilmGenres(long filmId, Set<Long> genres) {
        for (Long i : genres) {
            insertWithoutId(ADD_GENRES_QUERY, filmId, i);
        }
    }

    public void updateFilmGenres(long filmId, Set<Long> genres) {
        removeFilmGenres(filmId);
        addFilmGenres(filmId, genres);
    }

}
