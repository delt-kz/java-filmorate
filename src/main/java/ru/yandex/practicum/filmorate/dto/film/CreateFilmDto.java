package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
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
public class CreateFilmDto {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не может быть больше 200 символов")
    private String description;
    @NotNull(message = "Дата релиза не быть пустой")
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность не может быть пустой")
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    @NotNull(message = "Рейтинг не может быть пустым")
    private Rating mpa;
    private List<Genre> genres = new ArrayList<>();
}
