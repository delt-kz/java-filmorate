package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.validation.ReleaseDateConstraint;

import java.time.LocalDate;

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

    public boolean ф
}
