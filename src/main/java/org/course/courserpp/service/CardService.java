package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Card;
import org.course.courserpp.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findById(String id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Карта не найдена: " + id));
    }

    public Card save(Card card) {
        validate(card);
        return cardRepository.save(card);
    }

    public void delete(String id) {
        cardRepository.deleteById(id);
    }

    public List<Card> findByUser(String userId) {
        return cardRepository.findAllByUserId(userId);
    }

    private void validate(Card card) {
        if (card.getNumber() <= 0) {
            throw new IllegalArgumentException("Номер карты должен быть положительным");
        }
        if (card.getBalance() < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }
        if (card.getCVC() == null || card.getCVC().isBlank()) {
            throw new IllegalArgumentException("CVC не может быть пустым");
        }
    }
}
