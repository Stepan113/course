package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.ApplicationForCash;
import org.course.courserpp.repository.ApplicationForCashRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationForCashService {

    private final ApplicationForCashRepository applicationForCashRepository;

    public List<ApplicationForCash> findAll() {
        return applicationForCashRepository.findAll();
    }

    public ApplicationForCash findById(String id) {
        return applicationForCashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена: " + id));
    }

    public ApplicationForCash save(ApplicationForCash application) {
        validate(application);
        return applicationForCashRepository.save(application);
    }

    public void delete(String id) {
        applicationForCashRepository.deleteById(id);
    }

    private void validate(ApplicationForCash application) {
        if (application.getDelay() == null || application.getDelay() <= 0) {
            throw new IllegalArgumentException("Задержка должна быть больше нуля");
        }
        if (application.getWithdrawalAmount() == null || application.getWithdrawalAmount() <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть больше нуля");
        }
    }
}
