package ru.yandex.practicum.filmorate.storage.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BaseDbStorage<T> {
    private final JdbcTemplate jdbc;
    private final RowMapper<T> mapper;

    protected Optional<T> findOne(String sql, Object... params) {
        try {
            T result = jdbc.queryForObject(sql, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String sql, Object... params) {
        return jdbc.query(sql, mapper, params);
    }

    protected long insert(String sql, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected boolean update(String sql, Object... params) {
        int i = jdbc.update(sql);
        return i > 0;
    }

    protected boolean delete(String sql, Long id) {
        int i = jdbc.update(sql, id);
        return i > 0;
    }
}
