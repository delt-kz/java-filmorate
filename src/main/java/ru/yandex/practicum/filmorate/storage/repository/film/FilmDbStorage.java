package ru.yandex.practicum.filmorate.storage.repository.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.repository.BaseDbStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, r.id AS rating_id, r.name AS rating_name" +
            " FROM films AS f" +
            " LEFT JOIN ratings AS r ON f.rating_id = r.id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, r.id AS rating_id, r.name AS rating_name" +
            " FROM films AS f" +
            " LEFT JOIN ratings AS r ON f.rating_id = r.id" +
            " WHERE f.id = ?";
    private static final String GET_POPULAR = " SELECT f.*, r.id AS rating_id, r.name AS rating_name" +
            " FROM films f" +
            " LEFT JOIN films_likes AS fl ON f.id = fl.film_id" +
            " LEFT JOIN ratings AS r ON f.rating_id = r.id" +
            " GROUP BY f.id" +
            " ORDER BY COUNT(fl.user_id) DESC" +
            " LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, release_date, duration, rating_id)  VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film put(Film film) {
        Long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        return film;
    }

    public boolean update(Film film) {
        return update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    public Collection<Film> getPopular(int count) {
        return findMany(GET_POPULAR, count);
    }
}
