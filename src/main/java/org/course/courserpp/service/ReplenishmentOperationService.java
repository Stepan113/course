package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.ReplenishmentOperation;
import org.course.courserpp.repository.ReplenishmentOperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplenishmentOperationService {

    private final ReplenishmentOperationRepository replenishmentOperationRepository;

    public List<ReplenishmentOperation> findAll() {
        return replenishmentOperationRepository.findAll();
    }

    public ReplenishmentOperation findById(String id) {
        return replenishmentOperationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Операция пополнения не найдена: " + id));
    }

    public ReplenishmentOperation save(ReplenishmentOperation operation) {
        validate(operation);
        return replenishmentOperationRepository.save(operation);
    }

    public void delete(String id) {
        replenishmentOperationRepository.deleteById(id);
    }

    private void validate(ReplenishmentOperation operation) {
        if (operation.getSum() == null || operation.getSum() <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля");
        }
    }
}
