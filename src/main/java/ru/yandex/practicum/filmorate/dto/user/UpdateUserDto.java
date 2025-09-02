package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UpdateUserDto {
    @NotNull(message = "id пользователя для обновления должен быть указан")
    private Long id;
    @Email(message = "Неверный формат электронного адреса")
    private String email;
    @Pattern(regexp = "\\S+", message = "Логин пользователя не может содержать пробел")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return ! (login == null || login.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return ! (email == null || email.isBlank());
    }
}
