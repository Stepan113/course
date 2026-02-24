package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Score;
import org.course.courserpp.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public List<Score> findAll() {
        return scoreRepository.findAll();
    }

    public Score findById(String id) {
        return scoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Счёт не найден: " + id));
    }

    public Score save(Score score) {
        validate(score);
        return scoreRepository.save(score);
    }

    public void delete(String id) {
        scoreRepository.deleteById(id);
    }

    public List<Score> findByUser(String userId) {
        return scoreRepository.findAllByUserId(userId);
    }

    private void validate(Score score) {
        if (score.getNumber() == null || score.getNumber().isBlank()) {
            throw new IllegalArgumentException("Номер счёта не может быть пустым");
        }
        if (score.getBalance() == null || score.getBalance().isBlank()) {
            throw new IllegalArgumentException("Баланс не может быть пустым");
        }
        if (score.getCurrency() <= 0) {
            throw new IllegalArgumentException("Валюта должна быть положительной");
        }
    }
}
