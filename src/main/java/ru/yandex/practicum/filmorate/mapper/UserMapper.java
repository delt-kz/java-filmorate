package ru.yandex.practicum.filmorate.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(CreateUserDto createUser) {
        User user = new User();
        user.setEmail(createUser.getEmail());
        user.setLogin(createUser.getLogin());
        user.setName(createUser.getName());
        user.setBirthday(createUser.getBirthday());
        return user;
    }

    public static User updateUserFields(UpdateUserDto updateUser, User user) {
        if (updateUser.hasEmail()) {
            user.setEmail(updateUser.getEmail());
        }

        if (updateUser.hasLogin()) {
            user.setLogin(updateUser.getLogin());
        }

        if (updateUser.hasName()) {
            user.setName(updateUser.getName());
        }

        if (updateUser.hasBirthday()) {
            user.setBirthday(updateUser.getBirthday());
        }

        return user;
    }
}
