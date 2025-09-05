package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mapper.RatingRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseDbStorage<Rating> {
    private static final String FIND_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";

    public RatingDbStorage(JdbcTemplate jdbc, RatingRowMapper mapper)  {
        super(jdbc, mapper);
    }

    public Optional<Rating> get(long ratingId) {
        return findOne(FIND_QUERY, ratingId);
    }

    public List<Rating> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
