package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String ADD_QUERY = "INSERT INTO genres (name) VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE id = ?";

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
}
