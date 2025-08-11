package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotNull(message = "Электронный адрес не может быть пустым")
    @Email(message = "Неверный формат электронного адреса")
    private String email;
    @NotNull(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин пользователя не может содержать пробел")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
