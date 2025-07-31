package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController controller;

    @BeforeEach
    public void setup() {
        controller = new UserController();
    }

    @Test
    public void shouldAddUser() {
        User user = getValidUser();
        User added = controller.addUser(user);

        assertNotNull(added.getId());
        assertEquals("test@test.com", added.getEmail());
    }

    @Test
    public void shouldSetLoginAsNameIfNameIsBlank() {
        User user = getValidUser();
        user.setName(" ");
        User added = controller.addUser(user);

        assertEquals(user.getLogin(), added.getName());
    }

    @Test
    public void shouldThrowWhenEmailIsBlank() {
        User user = getValidUser();
        user.setEmail(" ");

        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    public void shouldThrowWhenEmailMissingAtSymbol() {
        User user = getValidUser();
        user.setEmail("invalidemail.com");

        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    public void shouldThrowWhenLoginIsBlank() {
        User user = getValidUser();
        user.setLogin("  ");

        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    public void shouldThrowWhenLoginHasSpaces() {
        User user = getValidUser();
        user.setLogin("bad login");

        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    public void shouldThrowWhenBirthdayInFuture() {
        User user = getValidUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    public void shouldThrowWhenEmailAlreadyExists() {
        User user1 = getValidUser();
        User user2 = getValidUser();
        user2.setLogin("otherLogin");

        controller.addUser(user1);

        assertThrows(ValidationException.class, () -> controller.addUser(user2));
    }

    @Test
    public void shouldUpdateUserSuccessfully() {
        User user = controller.addUser(getValidUser());
        user.setName("Updated Name");

        User updated = controller.updateUser(user);

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    public void shouldThrowWhenUpdatingNonExistingUser() {
        User user = getValidUser();
        user.setId(999L);

        assertThrows(NotFoundException.class, () -> controller.updateUser(user));
    }

    @Test
    public void shouldReturnAllUsers() {
        controller.addUser(getValidUser());
        controller.addUser(getAnotherValidUser());

        Collection<User> users = controller.getAllUsers();
        assertEquals(2, users.size());
    }

    private User getValidUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

    private User getAnotherValidUser() {
        User user = new User();
        user.setEmail("other@test.com");
        user.setLogin("otherlogin");
        user.setName("Other User");
        user.setBirthday(LocalDate.of(1990, 5, 10));
        return user;
    }
}
