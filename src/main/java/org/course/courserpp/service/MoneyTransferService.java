package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.MoneyTransfer;
import org.course.courserpp.model.Score;
import org.course.courserpp.repository.MoneyTransferRepository;
import org.course.courserpp.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;
    private final ScoreRepository scoreRepository;

    public List<MoneyTransfer> findAll() {
        return moneyTransferRepository.findAll();
    }

    public MoneyTransfer findById(String id) {
        return moneyTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Перевод не найден: " + id));
    }

    public MoneyTransfer save(MoneyTransfer moneyTransfer) {
        validate(moneyTransfer);
        return moneyTransferRepository.save(moneyTransfer);
    }

    public void delete(String id) {
        moneyTransferRepository.deleteById(id);
    }

    public void linkToScores(String moneyTransferId, List<String> scoreIds) {
        MoneyTransfer moneyTransfer = findById(moneyTransferId);
        for (String scoreId : scoreIds) {
            Score score = scoreRepository.findById(scoreId)
                    .orElseThrow(() -> new RuntimeException("Счёт не найден: " + scoreId));
            score.getMoneyTransfers().add(moneyTransfer);
            scoreRepository.save(score);
        }
    }

    private void validate(MoneyTransfer moneyTransfer) {
        if (moneyTransfer.getSum() == null || moneyTransfer.getSum() <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }
    }
}
