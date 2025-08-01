package archive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    private FilmController controller;

    @BeforeEach
    public void setUp() {
        controller = new FilmController();
    }

	@Test
	public void shouldAddFilm() {
		Film film = getValidFilm();
        assertDoesNotThrow(() -> controller.addFilm(film));
	}

    @Test
    public void shouldThrowWhenNameIsBlank() {
        Film film = getValidFilm();
        film.setName("  ");

        assertThrows(MethodArgumentNotValidException.class, () -> controller.addFilm(film));
    }

    @Test
    public void shouldThrowWhenDescriptionTooLong() {
        Film film = getValidFilm();
        film.setDescription("a".repeat(201));

        assertThrows(MethodArgumentNotValidException.class, () -> controller.addFilm(film));
    }

    @Test
    public void shouldThrowWhenReleaseDateIsTooOld() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        assertThrows(MethodArgumentNotValidException.class, () -> controller.addFilm(film));
    }

    @Test
    public void shouldThrowWhenDurationIsNegative() {
        Film film = getValidFilm();
        film.setDuration(-90);

        assertThrows(MethodArgumentNotValidException.class, () -> controller.addFilm(film));
    }

    private Film getValidFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("Mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        return film;
    }

}
