package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validation.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateFilmDto {
    @NotNull(message = "id пользователя для обновления должен быть указан")
    private Long id;
    private String name;
    @Size(max = 200, message = "Описание фильма не может быть больше 200 символов")
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private Rating mpa;
    private List<Genre> genres = new ArrayList<>();

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return ! (releaseDate == null);
    }

    public boolean hasDuration() {
        return ! (duration == null);
    }

    public boolean hasRating() {
        return ! (mpa == null);
    }

    public boolean hasGenres() {
        return ! (genres == null || genres.isEmpty());
    }
}
