package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotNull(message = "Электронный адрес не может быть пустым")
    @Email(message = "Неверный формат электронной адреса")
    private String email;
    @NotNull(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "\\S+", message = "...")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
