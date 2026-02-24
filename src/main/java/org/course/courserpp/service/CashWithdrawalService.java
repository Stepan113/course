package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.course.courserpp.model.ApplicationForCash;
import org.course.courserpp.model.CashWithdrawal;
import org.course.courserpp.model.Score;
import org.course.courserpp.repository.ApplicationForCashRepository;
import org.course.courserpp.repository.CashWithdrawalRepository;
import org.course.courserpp.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashWithdrawalService {

    private final CashWithdrawalRepository cashWithdrawalRepository;
    private final ScoreRepository scoreRepository;
    private final ApplicationForCashRepository applicationForCashRepository;

    public List<CashWithdrawal> findAll() {
        return cashWithdrawalRepository.findAll();
    }

    public CashWithdrawal findById(String id) {
        return cashWithdrawalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Снятие наличных не найдено: " + id));
    }

    public CashWithdrawal save(CashWithdrawal cashWithdrawal) {
        validate(cashWithdrawal);
        return cashWithdrawalRepository.save(cashWithdrawal);
    }

    public void delete(String id) {
        cashWithdrawalRepository.deleteById(id);
    }

    public void linkToScores(String cashWithdrawalId, List<String> scoreIds) {
        CashWithdrawal cashWithdrawal = findById(cashWithdrawalId);
        for (String scoreId : scoreIds) {
            Score score = scoreRepository.findById(scoreId)
                    .orElseThrow(() -> new RuntimeException("Счёт не найден: " + scoreId));
            try {
                long scoreBalance = Long.parseLong(score.getBalance().trim());
                if (scoreBalance < cashWithdrawal.getWithdrawalAmount()) {
                    throw new IllegalStateException("Недостаточно средств на счёте " + score.getNumber() + ": баланс " + scoreBalance + ", требуется " + cashWithdrawal.getWithdrawalAmount());
                }
            } catch (NumberFormatException e) {
                log.warn("Не удалось проверить баланс счёта {}: значение '{}' не является числом", score.getNumber(), score.getBalance());
            }
            score.getCashWithdrawals().add(cashWithdrawal);
            scoreRepository.save(score);
        }
    }

    public void linkToApplication(String cashWithdrawalId, String applicationId) {
        CashWithdrawal cashWithdrawal = findById(cashWithdrawalId);
        ApplicationForCash application = applicationForCashRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + applicationId));
        cashWithdrawal.setApplicationForCash(application);
        cashWithdrawalRepository.save(cashWithdrawal);
    }

    private void validate(CashWithdrawal cashWithdrawal) {
        if (cashWithdrawal.getWithdrawalAmount() == null || cashWithdrawal.getWithdrawalAmount() <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть больше нуля");
        }
    }
}
