package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
}
