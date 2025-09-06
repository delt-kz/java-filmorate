package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotNull(message = "Электронный адрес не может быть пустым")
    @Email(message = "Неверный формат электронного адреса")
    private String email;
    @NotNull(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин пользователя не может содержать пробел")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
