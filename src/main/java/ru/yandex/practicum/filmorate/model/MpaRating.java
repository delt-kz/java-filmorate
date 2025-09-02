package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public enum MpaRating {
    G(1),
    PG(2),
    PG13(3),
    R(4),
    NC17(5);

    private final int id;

    MpaRating(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MpaRating fromId(int id) {
        for(MpaRating rating : values()) {
            if (id == rating.id) {
                return rating;
            }
        }
        throw new NotFoundException("Рейтинг не найден в id " + id);
    }
}
