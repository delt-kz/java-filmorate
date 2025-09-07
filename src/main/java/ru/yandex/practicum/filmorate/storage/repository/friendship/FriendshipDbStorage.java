package ru.yandex.practicum.filmorate.storage.repository.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.repository.BaseDbStorage;

import java.util.List;

@Repository
public class FriendshipDbStorage extends BaseDbStorage<Long> implements FriendshipStorage {
    private static final RowMapper<Long> longMapper = (rs, rowNum) -> rs.getLong("friend_id");
    private static final String ADD_QUERY = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String GET_QUERY = "SELECT friend_id FROM friendships WHERE user_id = ?";
    private static final String GET_COMMON_QUERY = "SELECT f1.friend_id AS friend_id" +
            " FROM friendships AS f1" +
            " JOIN friendships AS f2" +
            "  ON f1.friend_id = f2.friend_id" +
            " WHERE f1.user_id = ?" +
            "  AND f2.user_id = ?" +
            "  AND f1.status = 'ACCEPTED'" +
            "  AND f2.status = 'ACCEPTED';";

    public FriendshipDbStorage(JdbcTemplate jdbc) {
        super(jdbc, longMapper);
    }

    public boolean add(long userId, long friendId) {
        return update(ADD_QUERY, userId, friendId);
    }

    public boolean delete(long userId, long friendId) {
        return delete(DELETE_QUERY, userId, friendId);
    }

    public List<Long> get(long userId) {
        return findMany(GET_QUERY, userId);
    }

    public List<Long> getCommon(long userId, long friendId) {
        return findMany(GET_COMMON_QUERY, userId, friendId);
    }
}
