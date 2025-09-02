package ru.yandex.practicum.filmorate.storage.repository.user;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.repository.BaseDbStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public class UserDbStorage extends BaseDbStorage implements UserStorage {
    private final static String FIND_ALL_QUERY = "SELECT * FROM users";
    private final static String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private final static String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)  VALUES (?,?,?,?)";
    private final static String UPDATE_QUERY = "UPDATE FROM users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public User put(User user) {
        Long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        user.setId(id);
        return user;
    }

    public boolean update(User user) {
        return update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }
}
