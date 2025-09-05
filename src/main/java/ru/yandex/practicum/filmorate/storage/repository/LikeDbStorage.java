package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeDbStorage extends BaseDbStorage<Long> {
    private static final RowMapper<Long> longMapper = (rs, rowNum) -> rs.getLong("user_id");
    private static final String ADD_QUERY = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_FILM_LIKES_QUERY = "SELECT * FROM films_likes WHERE film_id = ?";

    public LikeDbStorage(JdbcTemplate jdbc) {
        super(jdbc, longMapper);
    }

    public void add(long filmId, long userId) {
        insertWithoutId(ADD_QUERY, filmId, userId);
    }

    public boolean delete(long filmId, long userId) {
        return delete(DELETE_QUERY, filmId, userId);
    }

    public List<Long> getFilmLikes(long filmId) {
        return findMany(GET_FILM_LIKES_QUERY, filmId);
    }
}
