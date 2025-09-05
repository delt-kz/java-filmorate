package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class GenreFilmDbStorage extends BaseDbStorage<Long> {
    private static final RowMapper<Long> longMapper = (rs, rowNum) -> rs.getLong("genre_id");
    private static final String ADD_GENRES_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)";
    private static final String REMOVE_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String FIND_GENRES_QUERY = "SELECT genre_id FROM films_genres WHERE film_id = ?";
    private static final String FIND_FILMS_QUERY = "SELECT film_id FROM films_genres WHERE genre_id = ?";

    public GenreFilmDbStorage(JdbcTemplate jdbc) {
        super(jdbc, longMapper);
    }

    public void addFilmGenres(long filmId, Set<Long> genres) {
        for (Long i : genres) {
            insertWithoutId(ADD_GENRES_QUERY, filmId, i);
        }
    }

    public void removeFilmGenres(long filmId) {
        delete(REMOVE_GENRES_QUERY, filmId);
    }

    public void updateFilmGenres(long filmId, Set<Long> genres) {
        removeFilmGenres(filmId);
        addFilmGenres(filmId, genres);
    }

    public Set<Long> getFilmGenres(long filmId) {
        return Set.copyOf(findMany(FIND_GENRES_QUERY, filmId));
    }

    public Set<Long> getGenreFilms(long genreId) {
        return Set.copyOf(findMany(FIND_FILMS_QUERY, genreId));
    }
}
