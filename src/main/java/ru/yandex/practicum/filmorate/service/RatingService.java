package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.repository.RatingDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingDbStorage ratingStorage;

    public Rating get(long ratingId) {
        return ratingStorage.get(ratingId).orElseThrow(() -> new NotFoundException("Рейтинга с id=" + ratingId + " не существует"));
    }

    public List<Rating> findAll() {
        return ratingStorage.getAll();
    }
}
